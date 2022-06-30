package eu.pinske.playground.mq.websphere;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.resource.spi.ResourceAdapter;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.jca.support.ResourceAdapterFactoryBean;
import org.springframework.jca.work.SimpleTaskWorkManager;
import org.springframework.jms.config.DefaultJcaListenerContainerFactory;
import org.springframework.jms.listener.endpoint.DefaultJmsActivationSpecFactory;
import org.springframework.transaction.PlatformTransactionManager;

import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.spring.boot.MQConfigurationProperties;

import eu.pinske.playground.mq.MqConfiguration;

/**
 * https://www.ibm.com/support/pages/apar/IT38380
 */
@Configuration
@Profile("websphere-mq")
public class WebsphereMqConfiguration extends MqConfiguration {

    @Bean
    public ResourceAdapterFactoryBean mqResourceAdapter(AsyncTaskExecutor taskExecutor,
            @Value("com.ibm.mq.connector.ResourceAdapterImpl") Class<? extends ResourceAdapter> raClass) {
        SimpleTaskWorkManager workManager = new SimpleTaskWorkManager();
        workManager.setAsyncTaskExecutor(taskExecutor);

        ResourceAdapterFactoryBean resourceAdapterFactoryBean = new ResourceAdapterFactoryBean();
        resourceAdapterFactoryBean.setResourceAdapterClass(raClass);
        resourceAdapterFactoryBean.setWorkManager(workManager);
        return resourceAdapterFactoryBean;
    }

    @Bean
    public DefaultJcaListenerContainerFactory jmsListenerContainerFactory(ResourceAdapter mqResourceAdapter,
            PlatformTransactionManager transactionManager, MQConfigurationProperties mqProperties) {
        DefaultJcaListenerContainerFactory jcaListenerContainerFactory = new DefaultJcaListenerContainerFactory();
        jcaListenerContainerFactory.setResourceAdapter(mqResourceAdapter);
        jcaListenerContainerFactory.setTransactionManager(transactionManager);
        DefaultJmsActivationSpecFactory activationSpecFactory = new DefaultJmsActivationSpecFactory() {
            @Override
            protected void applyAcknowledgeMode(BeanWrapper bw, int ackMode) {
            }
        };
        Map<String, String> properties = new HashMap<>();
        properties.put("channel", mqProperties.getChannel());
        properties.put("userName", mqProperties.getUser());
        properties.put("password", mqProperties.getPassword());
        activationSpecFactory.setDefaultProperties(properties);
        jcaListenerContainerFactory.setActivationSpecFactory(activationSpecFactory);
        return jcaListenerContainerFactory;
    }

    @Override
    protected Queue queue(String name) {
        try {
            return new MQQueue(name);
        } catch (JMSException e) {
            throw new IllegalStateException(e);
        }
    }
}
