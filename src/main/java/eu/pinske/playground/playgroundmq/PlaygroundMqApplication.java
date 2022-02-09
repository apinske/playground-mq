package eu.pinske.playground.playgroundmq;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class PlaygroundMqApplication {
    static {
        System.setProperty("com.ibm.msg.client.commonservices.log.outputName", "./target/mq.log");
        System.setProperty("com.ibm.msg.client.commonservices.trace.outputName", "./target/mq.trace");
        System.setProperty("com.ibm.msg.client.commonservices.trace.exclude", "ALL");
        System.setProperty("com.ibm.msg.client.commonservices.trace.include",
                "com.ibm.msg.client.wmq.internal.WMQPoison");
        // com.ibm.msg.client.services.Trace.setOn();
    }

    public static void main(String[] args) {
        SpringApplication.run(PlaygroundMqApplication.class, args);
    }

    @Component
    public static class Listener {
        private static Logger log = LoggerFactory.getLogger(Listener.class);

        @Transactional
        @JmsListener(destination = "DEV.QUEUE.1", selector = "JMSXDeliveryCount < 3")
        public void onMessage(Message m) throws JMSException {
            String id = m.getJMSMessageID();
            int delivery = m.getIntProperty("JMSXDeliveryCount");
            String text = ((TextMessage) m).getText();
            log.info("received message: {} ({}): {}", id, delivery, text);
            throw new RuntimeException("temp error");
        }

        @Transactional
        @JmsListener(destination = "DEV.QUEUE.2")
        public void onBackoutMessage(Message m) throws JMSException {
            log.info("received backout message: {}", m.getJMSMessageID(), ((TextMessage) m).getText());
        }
    }

    @Bean
    @ConditionalOnProperty("playground.mq-workaround.enabled")
    public SimpleMessageListenerContainer backoutMoveListener(
            @Qualifier("nonXaJmsConnectionFactory") ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer smlc = new SimpleMessageListenerContainer();
        smlc.setConnectLazily(true);
        smlc.setConnectionFactory(connectionFactory);
        smlc.setDestinationName("DEV.QUEUE.1");
        smlc.setMessageSelector("NOT(JMSXDeliveryCount < 3)");
        return smlc;
    }
}
