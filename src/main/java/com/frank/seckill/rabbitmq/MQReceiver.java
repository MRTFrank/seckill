package com.frank.seckill.rabbitmq;

import com.frank.seckill.pojo.SeckillMessage;
import com.frank.seckill.pojo.SeckillOrder;
import com.frank.seckill.pojo.User;
import com.frank.seckill.service.IGoodsService;
import com.frank.seckill.service.IOrderService;
import com.frank.seckill.utils.JsonUtil;
import com.frank.seckill.vo.GoodsVO;
import com.frank.seckill.vo.RespBean;
import com.frank.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author: zhaobin
 * @date: 2021/7/15
 */
@Service
@Slf4j
public class MQReceiver {
//
//    @RabbitListener(queues = "queue")
//    public void receive(Object msg){
//        log.info("receive:"+msg);
//        //System.out.println();
//    }
//
//    @RabbitListener(queues = "queue_fanout01")
//    public void receive01(Object msg){
//        log.info("Queue01: receive:"+msg);
//        //System.out.println();
//    }
//
//    @RabbitListener(queues = "queue_fanout02")
//    public void receive02(Object msg){
//        log.info("Queue02: receive:"+msg);
//        //System.out.println();
//    }

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IOrderService orderService;
    /**
     * 下单
     */
    @RabbitListener(queues = "seckillQueue")
    public void receive(String message){
        log.info("接收信息：" + message);
        SeckillMessage seckillMessage = JsonUtil.stringToBean(message, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
        GoodsVO goodsVO = goodsService.findGoodsVOByGoodsId(goodsId);
        if(goodsVO.getStockCount() < 1){
            return;
        }

        // 判断是否重复抢购
        SeckillOrder seckillOrder =
                (SeckillOrder)redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            return;
        }
        //下单操作
        orderService.seckill(user,goodsVO);

    }

}
