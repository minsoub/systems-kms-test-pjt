package com.bithumbsystems.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RedisConfigTest {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void redis_insert_test() throws Exception {
        ValueOperations<String, String> data = redisTemplate.opsForValue();
        data.set("000001", "test 01");
        data.set("000002", "test 02");
        data.set("000003", "test 03");

        String value = data.get("000002");
        assertThat(value).isEqualTo("test 02");
    }
}

