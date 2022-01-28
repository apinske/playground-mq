package eu.pinske.playground.playgroundmq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.Lifecycle;
import org.springframework.context.weaving.AspectJWeavingEnabler;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.core.JmsOperations;
import org.springframework.transaction.support.TransactionOperations;

@SpringBootTest
class PlaygroundMqApplicationTests {
    static {
        if (InstrumentationLoadTimeWeaver.isInstrumentationAvailable()) {
            AspectJWeavingEnabler.enableAspectJWeaving(null, PlaygroundMqApplication.class.getClassLoader());
        }
    }

    @Autowired
    private TransactionOperations tx;

    @Autowired
    private JmsOperations jms;

    @Autowired
    private JmsListenerEndpointRegistry listeners;

    @Test
    void contextLoads() throws InterruptedException {
        tx.executeWithoutResult(s -> jms.convertAndSend("Queue", "0123456789abcdefghijklmnopqrstuvwxyz"));
        Thread.sleep(5000);
        listeners.getListenerContainers().forEach(Lifecycle::stop);
        Thread.sleep(2000);
    }
}
