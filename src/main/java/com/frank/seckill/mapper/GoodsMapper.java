package com.frank.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.frank.seckill.pojo.Goods;
import com.frank.seckill.vo.GoodsVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhaobin
 * @since 2021-07-05
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    List<GoodsVO> findGoodsVO();

    GoodsVO findGoodsVOByGoodsId(Long goodsId);
}
