package com.frank.seckill.utils;

import java.util.UUID;


/**
 * UUID工具类用于生成session
 *
 * @author noodle
 */
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
