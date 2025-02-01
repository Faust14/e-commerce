package com.shop.user_service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class SecretKeyConfig {

    @Value("${secret.key}")
    private String secretKey;

    @Value("${secret.issuer}")
    private String secretIssuer;
}
