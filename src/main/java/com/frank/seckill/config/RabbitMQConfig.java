package com.frank.seckill.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author: zhaobin
 * @date: 2021/7/15
 */
@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue queue(){
        return new Queue("queue", true);
    }
}
