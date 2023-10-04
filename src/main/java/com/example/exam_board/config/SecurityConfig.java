package com.example.exam_board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.net.http.HttpRequest;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((request) ->
                        request
                                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                                .requestMatchers("/user/**", "/articles/lists").permitAll()
                                .anyRequest().authenticated())
                .formLogin((form)-> form
                                .loginPage("/user/loginForm")
                                .loginProcessingUrl("/login")
                                .usernameParameter("userId")
                                //.defaultSuccessUrl("/"))
                                .defaultSuccessUrl("/articles/lists",true))
                .logout(logout -> logout
                        .logoutSuccessUrl("/articles/lists")
                        .logoutUrl("/logout"))

                .csrf(csrf->csrf.disable());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
