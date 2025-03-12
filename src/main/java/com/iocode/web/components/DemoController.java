package com.iocode.web.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@EnableMethodSecurity
public class DemoController {

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Welcome to Oauth2 Resource Server.!";
    }

    @GetMapping("/persons")
//    @PreAuthorize("hasRole('ADMIN')")
    public List<Person> demo(@RequestParam(required = false) String param, Authentication auth) {
        log.info("List of User Roles and authorities {}", auth.getAuthorities());
        return List.of(new Person("John Doe", 30, "johndoe@example.com"));
    }

}