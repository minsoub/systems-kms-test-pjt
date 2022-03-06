package com.bithumbsystems.config;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ParameterStorePropertiesTest {
    @Autowired
    private ParameterStoreProperties properties;

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

//    @Test
//    void local_s3_test() throws Exception {
//        assertThat(properties.getS3name()).isEqualTo("test-bucket-jms");
//    }
    @Test
    void local_parameter_test() throws Exception {
        assertThat(properties.getDbUrl()).isEqualTo("test");
    }
}
