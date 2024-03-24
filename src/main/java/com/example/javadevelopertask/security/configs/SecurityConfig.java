package com.example.javadevelopertask.security.configs;

import com.example.javadevelopertask.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@Configuration
public class SecurityConfig implements WebSecurityConfigurer {
    private final UsersRepo userRepository;
    @Autowired
    public SecurityConfig(UsersRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void init(SecurityBuilder builder) throws Exception {

    }
    @Override
    public void configure(SecurityBuilder builder) throws Exception {

    }
}
