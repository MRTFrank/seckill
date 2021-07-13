package com.frank.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.frank.seckill.pojo.Goods;
import com.frank.seckill.vo.GoodsVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhaobin
 * @since 2021-07-05
 */
public interface IGoodsService extends IService<Goods> {

    List<GoodsVO> findGoodsVO();

    GoodsVO findGoodsVOByGoodsId(Long goodsId);
}
