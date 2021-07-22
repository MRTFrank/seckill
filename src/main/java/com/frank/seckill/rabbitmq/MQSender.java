package com.frank.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: zhaobin
 * @date: 2021/7/15
 */
@Service
@Slf4j
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
//
//    public void send(Object msg){
//        log.info("send message："+msg);
//        rabbitTemplate.convertAndSend("fanoutExchange","", msg);

    /**
     * 发送秒杀信息
     * @param message
     */
    public void sendSeckillMessage(String message){
        log.info("发送信息：" + message);
        rabbitTemplate.convertAndSend("seckillExchange", "seckill.message",message);
    }

    public void send(String message) {
        rabbitTemplate.convertAndSend("fanoutExchange","", message);
    }
}
