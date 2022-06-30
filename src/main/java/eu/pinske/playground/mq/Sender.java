package eu.pinske.playground.mq;

import javax.jms.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Sender {
    private static Logger log = LoggerFactory.getLogger(Sender.class);

    @Autowired
    private JmsTemplate jms;

    @Autowired
    private Queue queue1;

    @Transactional
    @GetMapping("/send")
    public void send(String message) {
        jms.convertAndSend(queue1, message);
        log.info("sent {}", message);
    }
}
