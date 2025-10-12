package com.techullurgy.howzapp.auth

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter

@Configuration
@EnableWebSecurity
class AuthConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests { authorizer ->
                authorizer
                    .anyRequest().authenticated()
            }
            .formLogin { d -> d.disable() }
            .csrf { csrf -> csrf.disable()}
            .headers { headers -> headers.frameOptions { it.disable() } }
            .addFilterBefore(HeaderFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}

class HeaderFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader("Authorization") ?: run {
            filterChain.doFilter(request, response)
        }

        if (token.toString().lowercase() == "bearer irsath") {
            val authentication = UsernamePasswordAuthenticationToken
                .authenticated("irsath user id", "", listOf())

            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }
}