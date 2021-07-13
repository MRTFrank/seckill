package com.frank.seckill.vo;

import com.frank.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: zhaobin
 * @date: 2021/7/13
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailVO {
    private User user;

    private GoodsVO goodsVO;

    private int seckillStatus;

    private int remainSeconds;
}
