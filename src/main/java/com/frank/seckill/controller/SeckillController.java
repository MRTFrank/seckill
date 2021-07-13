package com.frank.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.frank.seckill.pojo.Order;
import com.frank.seckill.pojo.SeckillOrder;
import com.frank.seckill.pojo.User;
import com.frank.seckill.service.IGoodsService;
import com.frank.seckill.service.IOrderService;
import com.frank.seckill.service.ISeckillOrderService;
import com.frank.seckill.vo.GoodsVO;
import com.frank.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: zhaobin
 * @date: 2021/7/10
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private IGoodsService iGoodsService;

    @Autowired
    private ISeckillOrderService iSeckillOrderService;

    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("/doSeckill")
    public String doSeckill(Model model, User user, Long goodsId) {
        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);
        GoodsVO goods = iGoodsService.findGoodsVOByGoodsId(goodsId);
        //库存
        if (goods.getGoodsStock() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "seckillFail";
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder = iSeckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if(seckillOrder !=null){
            model.addAttribute("errmsg",RespBeanEnum.REPEAT_ERROR.getMessage());
            return "seckillFail";
        }

        Order order = iOrderService.seckill(user,goods);

        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        return "orderDetail";

    }
}
