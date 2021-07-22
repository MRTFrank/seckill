package com.frank.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author: zhaobin
 * @date: 2021/7/3
 */
@ToString
@AllArgsConstructor
public enum RespBeanEnum {

    SUCCESS(200,"成功"),
    ERROR(500,"服务端异常"),
    LOGIN_ERROR(500210,"用户名或密码错误"),
    MOBILE_ERROR(500211,"手机号码格式错误"),
    BIND_ERROR(500101, "参数校验异常"),

    EMPTY_STOCK(500500,"库存不足"),
    REPEAT_ERROR(500501,"该商品仅限购买一件"),
    SESSION_ERROR(500502,"登录错误");

    private final Integer code;

    private final String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
