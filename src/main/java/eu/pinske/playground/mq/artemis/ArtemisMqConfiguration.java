package eu.pinske.playground.mq.artemis;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.jms.Queue;
import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.ResourceAdapter;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.apache.activemq.artemis.ra.ActiveMQResourceAdapter;
import org.apache.activemq.artemis.ra.inflow.ActiveMQActivationSpec;
import org.springframework.beans.BeanWrapper;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.jca.support.ResourceAdapterFactoryBean;
import org.springframework.jca.work.SimpleTaskWorkManager;
import org.springframework.jms.config.DefaultJcaListenerContainerFactory;
import org.springframework.jms.listener.endpoint.JmsActivationSpecConfig;
import org.springframework.jms.listener.endpoint.StandardJmsActivationSpecFactory;
import org.springframework.transaction.PlatformTransactionManager;

import eu.pinske.playground.mq.MqConfiguration;

@Configuration
@Profile("artemis-mq")
public class ArtemisMqConfiguration extends MqConfiguration {

    @Bean
    public ResourceAdapterFactoryBean mqResourceAdapter(AsyncTaskExecutor taskExecutor,
            ArtemisProperties artemisProperties) throws URISyntaxException {
        ActiveMQResourceAdapter ra = new ActiveMQResourceAdapter();
        ra.setConnectorClassName("org.apache.activemq.artemis.core.remoting.impl.invm.InVMConnectorFactory");
        if (artemisProperties.getBrokerUrl() != null) {
            ra.setConnectorClassName("org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory");
            URI brokerUrl = new URI(artemisProperties.getBrokerUrl());
            ra.setConnectionParameters(String.format("host=%s;port=%d", brokerUrl.getHost(), brokerUrl.getPort()));
            ra.setUserName(artemisProperties.getUser());
            ra.setPassword(artemisProperties.getPassword());
        }

        SimpleTaskWorkManager workManager = new SimpleTaskWorkManager();
        workManager.setAsyncTaskExecutor(taskExecutor);

        ResourceAdapterFactoryBean resourceAdapterFactoryBean = new ResourceAdapterFactoryBean();
        resourceAdapterFactoryBean.setResourceAdapter(ra);
        resourceAdapterFactoryBean.setWorkManager(workManager);
        return resourceAdapterFactoryBean;
    }

    @Bean
    public DefaultJcaListenerContainerFactory jmsListenerContainerFactory(ResourceAdapter mqResourceAdapter,
            PlatformTransactionManager transactionManager) {
        DefaultJcaListenerContainerFactory jcaListenerContainerFactory = new DefaultJcaListenerContainerFactory();
        jcaListenerContainerFactory.setResourceAdapter(mqResourceAdapter);
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

    @Override
    protected Queue queue(String name) {
        return new ActiveMQQueue(name);
    }
}
