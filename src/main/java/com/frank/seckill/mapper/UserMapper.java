package com.frank.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.frank.seckill.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhaobin
 * @since 2021-07-03
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
