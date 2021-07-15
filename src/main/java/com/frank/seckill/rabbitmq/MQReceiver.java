package com.frank.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: zhaobin
 * @date: 2021/7/15
 */
@Service
@Slf4j
public class MQReceiver {

    @RabbitListener(queues = "queue")
    public void receive(Object msg){
        log.info("receive:"+msg);
        //System.out.println();
    }
}
