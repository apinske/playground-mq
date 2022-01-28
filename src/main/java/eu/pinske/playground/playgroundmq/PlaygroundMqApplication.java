package eu.pinske.playground.playgroundmq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.weaving.AspectJWeavingEnabler;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import eu.pinske.playground.playgroundmq.data.Thing;
import eu.pinske.playground.playgroundmq.data.ThingRepository;

@SpringBootApplication
public class PlaygroundMqApplication {

    public static void main(String[] args) {
        AspectJWeavingEnabler.enableAspectJWeaving(null, PlaygroundMqApplication.class.getClassLoader());
        SpringApplication.run(PlaygroundMqApplication.class, args);
    }

    @Component
    public static class Listener {
        private static Logger log = LoggerFactory.getLogger(Listener.class);

        @Autowired
        private ThingRepository db;

        @Autowired
        private JmsTemplate jms;

        @Transactional
        @JmsListener(destination = "Queue", concurrency = "4")
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
