package eu.pinske.playground.mq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.Lifecycle;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("artemis-mq")
class PlaygroundMqApplicationTests {

    @Autowired
    private Sender sender;

    @Autowired
    private JmsListenerEndpointRegistry listeners;

    @Test
    void contextLoads() throws InterruptedException {
        Thread.sleep(2000);
        sender.send("0");
        Thread.sleep(5000);
        listeners.getListenerContainers().forEach(Lifecycle::stop);
        Thread.sleep(2000);
    }
}
