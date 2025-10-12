package com.techullurgy.howzapp.users.api

import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import com.techullurgy.howzapp.users.infra.database.repositories.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.uuid.Uuid

@RestController
@RequestMapping("/users")
class UserController(
    private val userRepository: UserRepository
) {

    @PostMapping("/user/{name}")
    fun createUser(
        @PathVariable("name") name: String,
    ): ResponseEntity<String> {
        val userId = Uuid.random().toString()
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