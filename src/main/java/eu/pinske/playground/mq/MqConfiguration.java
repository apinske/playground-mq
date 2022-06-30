package eu.pinske.playground.mq;

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public abstract class MqConfiguration {

    @Bean
    public Queue queue1(@Value("${playground.queue1}") String name) {
        return queue(name);
    }

    protected abstract Queue queue(String name);
}
