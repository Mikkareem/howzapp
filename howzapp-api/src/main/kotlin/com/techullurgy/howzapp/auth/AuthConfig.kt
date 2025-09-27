package com.techullurgy.howzapp.auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class AuthConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests { authorizer ->
                authorizer.anyRequest().permitAll()
            }
            .csrf { csrf -> csrf.disable()}
            .headers { headers -> headers.frameOptions { it.disable() } }
            .build()
    }
}