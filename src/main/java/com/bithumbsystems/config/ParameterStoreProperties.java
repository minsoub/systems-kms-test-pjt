package com.bithumbsystems.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@NoArgsConstructor
@Configuration
public class ParameterStoreProperties {

    @Value("${jdbcurl}")
    private String dbUrl;

//    @Value("${DB_PASS}")
//    private String dbPass;
//
//    @Value("${DB_ID}")
//    private String dbUser;

//    @Value("${test-bucket-jms}")
//    private String s3name;
}
