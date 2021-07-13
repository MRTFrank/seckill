package com.frank.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.frank.seckill.pojo.Order;
import com.frank.seckill.pojo.User;
import com.frank.seckill.vo.GoodsVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhaobin
 * @since 2021-07-05
 */
public interface IOrderService extends IService<Order> {

    Order seckill(User user, GoodsVO goods);
}
