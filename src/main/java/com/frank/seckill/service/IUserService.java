package com.frank.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.frank.seckill.pojo.User;
import com.frank.seckill.vo.LoginVO;
import com.frank.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhaobin
 * @since 2021-07-03
 */
public interface IUserService extends IService<User> {

    RespBean doLogin(LoginVO loginVO, HttpServletRequest request, HttpServletResponse response);

    User getUserByCookie(String userTicket,HttpServletRequest request, HttpServletResponse response);
}
