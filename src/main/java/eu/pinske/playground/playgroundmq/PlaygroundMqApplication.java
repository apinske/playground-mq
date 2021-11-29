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

// docker run --rm -e LICENSE=accept -e MQ_QMGR_NAME=QM1 -p 1414:1414 -p 9443:9443 -it ibmcom/mq
// curl -k -u admin:passw0rd https://localhost:9443/ibmmq/console/internal/ibmmq/qmgr/QM1/queue/DEV.QUEUE.1/messages -H 'Content-Type: application/json' -H 'ibm-mq-csrf-token: value' --data '{"message":"x"}'
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
