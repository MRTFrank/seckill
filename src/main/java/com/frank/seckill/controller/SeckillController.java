package com.frank.seckill.controller;

import com.frank.seckill.pojo.*;
import com.frank.seckill.rabbitmq.MQSender;
import com.frank.seckill.service.IGoodsService;
import com.frank.seckill.service.IOrderService;
import com.frank.seckill.service.ISeckillOrderService;
import com.frank.seckill.utils.JsonUtil;
import com.frank.seckill.vo.GoodsVO;
import com.frank.seckill.vo.RespBean;
import com.frank.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zhaobin
 * @date: 2021/7/10
 */
@RestController
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    private IGoodsService iGoodsService;

    @Autowired
    private ISeckillOrderService iSeckillOrderService;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MQSender mqSender;

    //false为有库存
    private Map<Long,Boolean> EmptyStockMap = new HashMap<>();

    @RequestMapping("/doSeckill")
    public RespBean doSeckill(Model model, User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 判断是否重复抢购
        SeckillOrder seckillOrder =
            (SeckillOrder)redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }

        //内存标记减少Redis的访问
        if(EmptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        //预减库存
        Long stock = valueOperations.decrement("seckillGoods" + goodsId);
        if(stock < 0){
            EmptyStockMap.put(goodsId,true);
            valueOperations.increment("seckillGoods" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtil.beanToString(seckillMessage));

        return RespBean.success(0);
        /*
        GoodsVO goods = iGoodsService.findGoodsVOByGoodsId(goodsId);
        // 库存
        if (goods.getGoodsStock() < 1) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        // 判断是否重复抢购
        SeckillOrder seckillOrder =
            (SeckillOrder)redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goods.getId());
        if (seckillOrder != null) {
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        
        Order order = iOrderService.seckill(user, goods);
        
        return RespBean.success(order);
        */

        //return null;

    }

    /**
     * 初始化,把商品库存数量加载到Redis
     * 
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVO> list = iGoodsService.findGoodsVO();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(
            goodsVO -> {
                redisTemplate.opsForValue().set("seckillGoods" + goodsVO.getId(), goodsVO.getStockCount());
                EmptyStockMap.put(goodsVO.getId(), false);
            });
    }
}
