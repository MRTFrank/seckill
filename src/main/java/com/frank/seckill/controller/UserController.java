package com.frank.seckill.controller;


import com.frank.seckill.pojo.User;
import com.frank.seckill.rabbitmq.MQSender;
import com.frank.seckill.vo.RespBean;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhaobin
 * @since 2021-07-03
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MQSender mqSender;
    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user){
        return RespBean.success(user);
    }


    /**
     * Test MQ
     */
    @RequestMapping("/mq")
    @ResponseBody
    public void mq(){
        mqSender.send("wangyujun");

    }

    @RequestMapping("/mq/fanout")
    @ResponseBody
    public void mq01(){
        mqSender.send("zhaobin");

    }
}
