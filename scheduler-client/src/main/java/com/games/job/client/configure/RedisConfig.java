package com.games.job.client.configure;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

@Configuration
public class RedisConfig {

    @Value("${spring.quartz.address}")
    private String address = "";

    @Value("${spring.redis.password}")
    private String password = "";

    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public ShardedJedisPool shardedJedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(4);

        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        String[] shardedAddress = address.split(",");
        for (String address : shardedAddress) {
            InetSocketAddress inetSocketAddress = getAddress(address);
            JedisShardInfo si = new JedisShardInfo(inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort());
            if(!StringUtils.isEmpty(password)) {
                si.setPassword(password);
            }
            shards.add(si);
        }
        log.info("QuartzRedisConfig@provide jedis Pool over");
        return new ShardedJedisPool(poolConfig, shards);
    }

    private   InetSocketAddress getAddress(String address) {
        int finalColon = address.lastIndexOf(':');
        if (finalColon < 1) {
            throw new IllegalArgumentException("Invalid address:" + address);

        }
        String hostPart = address.substring(0, finalColon);
        String portNum = address.substring(finalColon + 1);
        return new InetSocketAddress(hostPart, Integer.parseInt(portNum));
    }

}
