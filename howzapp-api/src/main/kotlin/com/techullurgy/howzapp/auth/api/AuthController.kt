package com.techullurgy.howzapp.auth.api

import com.techullurgy.howzapp.auth.dto.LoginRequest
import com.techullurgy.howzapp.auth.dto.RefreshRequest
import com.techullurgy.howzapp.auth.dto.RegisterRequest
import com.techullurgy.howzapp.auth.services.AuthService
import com.techullurgy.howzapp.users.models.AppUser
import com.techullurgy.howzapp.users.models.AuthenticatedUser
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/register")
    fun register(
        @RequestBody body: RegisterRequest
    ): AppUser {
        return authService.register(
            email = body.email,
            username = body.username,
            password = body.password
        )
    }

    @PostMapping("/login")
    fun login(
        @RequestBody body: LoginRequest
    ): AuthenticatedUser {
        return authService.login(
            email = body.email,
            password = body.password
        )
    }

    @PostMapping("/refresh")
    fun refresh(
        @RequestBody body: RefreshRequest
    ): AuthenticatedUser {
        return authService
            .refresh(body.refreshToken)
    }

    @PostMapping("/logout")
    fun logout(
        @RequestBody body: RefreshRequest
    ) {
        authService.logout(body.refreshToken)
    }
}