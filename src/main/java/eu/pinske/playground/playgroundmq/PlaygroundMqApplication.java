package eu.pinske.playground.playgroundmq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class PlaygroundMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaygroundMqApplication.class, args);
    }

    @Component
    public static class Listener {
        private static Logger log = LoggerFactory.getLogger(Listener.class);

        @JmsListener(destination = "DEV.QUEUE.1")
        public void onMessage(Message m) throws JMSException {
            log.info("received message: {}", ((TextMessage) m).getText());
            if (!m.getJMSRedelivered()) {
                throw new RuntimeException("temp error");
            }
        }
    }
}
