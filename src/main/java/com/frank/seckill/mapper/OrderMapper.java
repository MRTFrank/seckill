package com.frank.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.frank.seckill.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhaobin
 * @since 2021-07-05
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
