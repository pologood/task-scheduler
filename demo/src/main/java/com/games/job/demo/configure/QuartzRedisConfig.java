package com.games.job.demo.configure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

@Component
public class QuartzRedisConfig {

    @Value("${spring.quartz.redisAddress}")
    private String redisAddress = "";

    @Value("${spring.quartz.redisPassWord}")
    private String passWord = "";

    private static final Logger log = LoggerFactory.getLogger(QuartzRedisConfig.class);


    @Bean(name = "quartzJedisPool")
    public ShardedJedisPool provide() {

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(50);
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        String[] shardedAddress = redisAddress.split(",");
        for (String address : shardedAddress) {
            InetSocketAddress inetSocketAddress = getAddress(address);
            JedisShardInfo si = new JedisShardInfo(inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort());
            if(!StringUtils.isEmpty(passWord)) {
                si.setPassword(passWord);
            }
            shards.add(si);
        }
        log.info("QuartzRedisConfig@provide jedis Pool over");
        return new ShardedJedisPool(poolConfig, shards);
    }

    private   InetSocketAddress getAddress(String address) {
        int finalColon = address.lastIndexOf(':');
        if (finalColon < 1) {
            throw new IllegalArgumentException("Invalid address:"
                    + address);

        }
        String hostPart = address.substring(0, finalColon);
        String portNum = address.substring(finalColon + 1);
        return new InetSocketAddress(hostPart, Integer
                .parseInt(portNum));
    }

}
