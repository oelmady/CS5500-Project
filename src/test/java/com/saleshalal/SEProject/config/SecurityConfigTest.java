package com.saleshalal.SEProject.config;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SecurityConfigTest {

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Test
    public void contextLoads() {
        assertThat(springSecurityFilterChain).isNotNull();
    }
}
