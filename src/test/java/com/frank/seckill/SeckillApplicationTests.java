package com.frank.seckill;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SeckillApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisScript<Boolean> redisScript;

    @Test
    public void testLock01() {
        //如果key不存在才可以设置成功
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1");
        //如果占位成功，进行操作
        if(isLock){
            valueOperations.set("name","xxxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name = "+name);
            //操作结束，释放锁
            redisTemplate.delete("k1");
        }else{
            System.out.println("线程忙");
        }
    }

    @Test
    public void testLock03() {
        //如果key不存在才可以设置成功
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String s = UUID.randomUUID().toString();
        Boolean isLock = valueOperations.setIfAbsent("k1", s,120, TimeUnit.SECONDS);
        //如果占位成功，进行操作
        if(isLock){
            valueOperations.set("name","xxxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name = "+name);
            System.out.println(valueOperations.get("k1"));
            Boolean result =(Boolean) redisTemplate.execute(redisScript, Collections.singletonList("k1"), s);
            System.out.println(result);
        }else{
            System.out.println("线程忙");
        }
    }

}
