package com.techullurgy.howzapp.users.api

import com.techullurgy.howzapp.common.types.UserId
import com.techullurgy.howzapp.common.types.id
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import com.techullurgy.howzapp.users.infra.database.repositories.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val userRepository: UserRepository
) {

    @PostMapping("/user/{name}")
    fun createUser(
        @PathVariable("name") name: String,
    ): ResponseEntity<String> {
        val userId = UserId.id
        val entity = UserEntity(userId, name, "")
        userRepository.save(entity)
        return ResponseEntity.ok("Created $userId")
    }

    @GetMapping
    fun getAllUsers(): List<UserEntity> {
        val users = userRepository.findAll().toList()
        return users
    }
}