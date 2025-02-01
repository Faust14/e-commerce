package com.shop.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AuthClientConfig {

    @Value("${secret.key}")
    private String secretKey;

    @Value("${secret.issuer}")
    private String secretIssuer;

}
