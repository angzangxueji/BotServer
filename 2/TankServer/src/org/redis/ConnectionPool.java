package org.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class ConnectionPool {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final Integer SERVER_PORT = 6379;


    public  Jedis getConnection(){
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        JedisPool jedisPool = new JedisPool(poolConfig, "127.0.0.1", 6379);
        Jedis jedis=jedisPool.getResource();//使用连接池默认参数
        return jedis;
    }



}
