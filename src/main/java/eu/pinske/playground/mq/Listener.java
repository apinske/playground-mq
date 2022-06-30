package eu.pinske.playground.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import eu.pinske.playground.mq.data.Thing;
import eu.pinske.playground.mq.data.ThingRepository;

@Component
public class Listener {
    private static Logger log = LoggerFactory.getLogger(Listener.class);

    @Autowired
    private ThingRepository db;

    @Autowired
    private JmsTemplate jms;

    @Autowired
    private Queue queue1;

    @Transactional
    @JmsListener(destination = "${playground.queue1}", concurrency = "2")
    public void onMessage(Message m) throws JMSException {
        String message = ((TextMessage) m).getText();
        log.info("rcvd: {}", message);
        if (message.isEmpty()) {
            return;
        }
        Thing thing;
        if (!m.propertyExists("id")) {
            thing = new Thing();
            thing.setName(message);
            db.save(thing);
            log.info("   new {}", thing);
        } else {
            thing = db.findById(m.getLongProperty("id")).get();
            if (!thing.getName().equals(message)) {
                log.error("failed {}, expected {}", thing, message);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return;
            }
            thing.setName(message.substring(0, message.length() - 1));
            log.info("update {}", thing);
        }
        jms.send(queue1, s -> {
            TextMessage msg = s.createTextMessage(thing.getName());
            msg.setLongProperty("id", thing.getId());
            return msg;
        });
    }
}