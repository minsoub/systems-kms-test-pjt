package com.bithumbsystems.repository;


import com.bithumbsystems.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserRepository repository;

    @Test
    void redis_user_insert_test() throws Exception {
        User user = new User();
        user.setEmail("minsoub@gmail.com");
        user.setUsername("Joung minsoub");
        repository.save(user);

        List<User> list = (List<User>) repository.findAll();

        assertThat(list.size()).isNotEqualTo(0);
        assertThat(list.get(0).getEmail()).isEqualTo("minsoub@gmail.com");
    }
};

