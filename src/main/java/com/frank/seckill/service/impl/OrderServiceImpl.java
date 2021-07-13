package com.frank.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.frank.seckill.mapper.OrderMapper;
import com.frank.seckill.pojo.Order;
import com.frank.seckill.pojo.SeckillGoods;
import com.frank.seckill.pojo.SeckillOrder;
import com.frank.seckill.pojo.User;
import com.frank.seckill.service.IOrderService;
import com.frank.seckill.service.ISeckillGoodsService;
import com.frank.seckill.service.ISeckillOrderService;
import com.frank.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhaobin
 * @since 2021-07-05
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private ISeckillGoodsService iSeckillGoodsService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ISeckillOrderService iSeckillOrderService;


    @Override
    public Order seckill(User user, GoodsVO goods) {

        //秒杀商品表减库存
        SeckillGoods seckillGoods = iSeckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
        iSeckillGoodsService.updateById(seckillGoods);


        //生成订单
        Order orderInfo = new Order();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);// 订单中商品的数量
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSeckillPrice());// 秒杀价格
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderMapper.insert(orderInfo);
        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(orderInfo.getId());
        seckillOrder.setUserId(user.getId());
        iSeckillOrderService.save(seckillOrder);

        return orderInfo;
    }
}
