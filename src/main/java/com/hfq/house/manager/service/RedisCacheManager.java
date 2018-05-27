
package com.hfq.house.manager.service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;


/**
 * redis 配置
 * @author jjs
 *
 */
@Component("redisCacheManager")
public class RedisCacheManager {

//    @Resource(name = "hzfRedisTemplate")
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 存储key-value
     * 
     * @param key
     * @param value
     */
    public void putValue(String key, String value) {
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        valueOps.set(key, value);
    }

    /**
     * 存储key-value
     * 
     * @param key
     * @param value
     * @throws Exception add by arison
     */
    public void incValue(String key) throws Exception {
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        valueOps.increment(key, 1);
    }

    /**
     * 存储key-value
     * 
     * @param key
     * @param value
     * @param timeout 单位为毫秒
     * @throws Exception
     */
    public void putValue(String key, String value, long timeout) {
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        valueOps.set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 批量存储key-value
     * 
     * @param values
     * @throws Exception
     */
    public void putValue(Map<String, String> values) throws Exception {
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        valueOps.multiSet(values);
    }

    /**
     * 获取value
     * 
     * @param key
     * @return 不存在时，返回null
     * @throws Exception
     */
    public String getValue(String key) {
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        String result = valueOps.get(key);
        return StringUtils.isEmpty(result) ? null : result;
    }


    /**
     * 删除key
     * 
     * @param key
     * @throws Exception
     */
    public void delete(String key) throws Exception {
        stringRedisTemplate.delete(key);
    }


    /**
     * 存储 key-value
     * 
     * @param key
     * @param value
     * @throws Exception
     */
    public void leftPushList(String key, String value) throws Exception {
        ListOperations<String, String> listOps = stringRedisTemplate.opsForList();
        listOps.leftPush(key, value);
    }

    /**
     * 随机移除某个元素
     * 
     * @param key
     * @throws Exception
     */
    public void leftPopList(String key) throws Exception {
        ListOperations<String, String> listOps = stringRedisTemplate.opsForList();
        listOps.leftPop(key);
    }
    
    /**
     * 随机移除某个元素
     * 
     * @param key
     * @throws Exception
     */
    public String rightPop(String key)throws Exception{
        ListOperations<String, String> listOps = stringRedisTemplate.opsForList();
        return listOps.rightPop(key,0,TimeUnit.SECONDS);
    }




}
