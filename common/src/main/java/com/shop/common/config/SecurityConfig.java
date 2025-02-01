package com.shop.common.config;

import com.shop.common.auth.JwtAuthFilter;
import com.shop.common.auth.TokenUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@ComponentScan("com.shop.auth")
@Configuration
@Import({AuthClientConfig.class})
public class SecurityConfig {

    @Bean
    public AuthClientConfig authClientConfig() {
        return new AuthClientConfig();
    }

    @Bean
    public TokenUtil tokenUtil(AuthClientConfig authClientConfig) {
        return new TokenUtil(authClientConfig);
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter(TokenUtil tokenUtil) {
        return new JwtAuthFilter(tokenUtil);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
