package com.iocode.web.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Autowired
    private CustomOidcUserService customUserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.oauth2Login(oauth2 -> oauth2.userInfoEndpoint(
                userInfo -> {
                    userInfo.oidcUserService(customUserService);
                }));
        http.authorizeHttpRequests(auth ->
            auth.requestMatchers("/hello")
                .permitAll()
                .anyRequest()
                .authenticated());
        return  http.build();
    }

}
