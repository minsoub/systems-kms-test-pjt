package com.bithumbsystems.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@ToString
@RedisHash("users")
public class User {
    @Id
    private String id;
    private String username;
    private String email;
}
