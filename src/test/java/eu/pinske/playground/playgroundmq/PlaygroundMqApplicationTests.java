package eu.pinske.playground.playgroundmq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.transaction.support.TransactionTemplate;

// docker run --rm -e LICENSE=accept -e MQ_QMGR_NAME=QM1 -p 1414:1414 -p 9443:9443 -it ibmcom/mq
@SpringBootTest
class PlaygroundMqApplicationTests {

    @Autowired
    private TransactionTemplate tx;

    @Autowired
    private JmsTemplate jms;

    @Autowired
    private JmsListenerEndpointRegistry listeners;

    @Test
    void contextLoads() throws InterruptedException {
        Thread.sleep(5000);
        for (int i = 0; i < 5; i++) {
            send("msg");
        }
        Thread.sleep(20000);
        listeners.getListenerContainers().forEach(MessageListenerContainer::stop);
    }

    private void send(String msg) {
        tx.executeWithoutResult(x -> jms.send("DEV.QUEUE.1", s -> s.createTextMessage(msg)));
    }
}
