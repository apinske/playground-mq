package eu.pinske.playground.playgroundmq;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.ResourceAdapter;

import org.apache.activemq.artemis.ra.ActiveMQResourceAdapter;
import org.apache.activemq.artemis.ra.inflow.ActiveMQActivationSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.jca.support.ResourceAdapterFactoryBean;
import org.springframework.jca.work.SimpleTaskWorkManager;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJcaListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.endpoint.JmsActivationSpecConfig;
import org.springframework.jms.listener.endpoint.StandardJmsActivationSpecFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import eu.pinske.playground.playgroundmq.data.Thing;
import eu.pinske.playground.playgroundmq.data.ThingRepository;

@SpringBootApplication
public class PlaygroundMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaygroundMqApplication.class, args);
    }

    @Bean
    public ResourceAdapterFactoryBean artemisResourceAdapter(AsyncTaskExecutor taskExecutor) {
        ActiveMQResourceAdapter ra = new ActiveMQResourceAdapter();
        ra.setConnectorClassName("org.apache.activemq.artemis.core.remoting.impl.invm.InVMConnectorFactory");

        SimpleTaskWorkManager workManager = new SimpleTaskWorkManager();
        workManager.setAsyncTaskExecutor(taskExecutor);

        ResourceAdapterFactoryBean resourceAdapterFactoryBean = new ResourceAdapterFactoryBean();
        resourceAdapterFactoryBean.setResourceAdapter(ra);
        resourceAdapterFactoryBean.setWorkManager(workManager);
        return resourceAdapterFactoryBean;
    }

    @Bean
    public DefaultJcaListenerContainerFactory jmsListenerContainerFactory(ResourceAdapter artemisResourceAdapter,
            PlatformTransactionManager transactionManager) {
        DefaultJcaListenerContainerFactory jcaListenerContainerFactory = new DefaultJcaListenerContainerFactory();
        jcaListenerContainerFactory.setResourceAdapter(artemisResourceAdapter);
        jcaListenerContainerFactory.setTransactionManager(transactionManager);
        StandardJmsActivationSpecFactory activationSpecFactory = new StandardJmsActivationSpecFactory() {
            @Override
            public ActivationSpec createActivationSpec(ResourceAdapter adapter, JmsActivationSpecConfig config) {
                ActivationSpec activationSpec = super.createActivationSpec(adapter, config);
                try {
                    activationSpec.setResourceAdapter(adapter);
                } catch (ResourceException e) {
                    throw new IllegalStateException(e);
                }
                return activationSpec;
            }

            @Override
            protected void populateActivationSpecProperties(BeanWrapper bw, JmsActivationSpecConfig config) {
                super.populateActivationSpecProperties(bw, config);
                bw.setPropertyValue("maxSession", config.getMaxConcurrency());
            }
        };
        activationSpecFactory.setActivationSpecClass(ActiveMQActivationSpec.class);
        activationSpecFactory.setDefaultProperties(Map.of("useJNDI", "false"));
        jcaListenerContainerFactory.setActivationSpecFactory(activationSpecFactory);
        return jcaListenerContainerFactory;
    }

    @Component
    public static class Listener {
        private static Logger log = LoggerFactory.getLogger(Listener.class);

        @Autowired
        private ThingRepository db;

        @Autowired
        private JmsTemplate jms;

        @Transactional
        @JmsListener(destination = "Queue", concurrency = "2")
        public void onMessage(Message m) throws JMSException {
            String text = ((TextMessage) m).getText();
            if (text.isEmpty()) {
                return;
            }
            Thing thing;
            if (!m.propertyExists("id")) {
                thing = new Thing();
                thing.setName(text);
                db.save(thing);
                log.info("   new {}", thing);
            } else {
                thing = db.findById(m.getLongProperty("id")).get();
                if (!thing.getName().equals(text)) {
                    log.error("failed {}, expected {}", thing, text);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return;
                }
                thing.setName(text.substring(0, text.length() - 1));
                log.info("update {}", thing);
            }
            jms.send("Queue", s -> {
                TextMessage message = s.createTextMessage(thing.getName());
                message.setLongProperty("id", thing.getId());
                return message;
            });
        }
    }
}
