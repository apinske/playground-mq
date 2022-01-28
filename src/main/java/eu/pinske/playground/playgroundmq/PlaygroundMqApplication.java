package eu.pinske.playground.playgroundmq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootApplication
public class PlaygroundMqApplication {
    static {
        System.setProperty("com.ibm.msg.client.commonservices.log.outputName", "./target/mq.log");
        System.setProperty("com.ibm.msg.client.commonservices.trace.outputName", "./target/mq.trace");
        System.setProperty("com.ibm.msg.client.commonservices.trace.exclude", "ALL");
        System.setProperty("com.ibm.msg.client.commonservices.trace.include",
                "com.ibm.msg.client.wmq.internal.WMQPoison");
        // Trace.setOn();
    }

    public static void main(String[] args) {
        SpringApplication.run(PlaygroundMqApplication.class, args);
    }

    @Component
    public static class Listener {
        private static Logger log = LoggerFactory.getLogger(Listener.class);

        @Autowired
        private TransactionTemplate tx;

        @Autowired
        private JmsTemplate jms;

        @Transactional
        @JmsListener(destination = "DEV.QUEUE.1")
        public void onMessage(Message m) throws JMSException {
            String id = m.getJMSMessageID();
            int delivery = m.getIntProperty("JMSXDeliveryCount");
            String text = ((TextMessage) m).getText();
            log.info("received message: {} ({}): {}", id, delivery, text);

            if ("ok".equals(text) && delivery == 3) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCompletion(int status) {
                        findMsg("DEV.QUEUE.1", id);
                    }
                });
            }

            throw new RuntimeException("temp error");
        }

        private boolean findMsg(String q, String id) {
            return tx.execute(x -> jms.browse(q, (s, b) -> b.getEnumeration().hasMoreElements()));
        }

        @Transactional
        @JmsListener(destination = "DEV.QUEUE.2")
        public void onBackoutMessage(Message m) throws JMSException {
            log.info("received backout message: {}", m.getJMSMessageID(), ((TextMessage) m).getText());
        }
    }
}
