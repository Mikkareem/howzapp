package com.techullurgy.howzapp.test

import com.techullurgy.howzapp.chats.api.dto.MessageDto
import com.techullurgy.howzapp.chats.services.usecases.NewMessageUsecase
import com.techullurgy.howzapp.common.types.UserId
import com.techullurgy.howzapp.common.types.id
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import com.techullurgy.howzapp.users.infra.database.repositories.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TestCommandLineRunner(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,

    private val newMessageUsecase: NewMessageUsecase
): CommandLineRunner {

    @Transactional
    override fun run(vararg args: String?) {
        val irsath = userRepository.saveAndFlush(
            UserEntity(
                id = UserId.id,
                name = "Irsath Kareem",
                email = "irsath.kareem@howzapp.com",
                hashedPassword = passwordEncoder.encode("Irka@123"),
                profilePictureUrl = ""
            )
        )

        val faisal = userRepository.saveAndFlush(
            UserEntity(
                id = UserId.id,
                name = "Hawath Faisal",
                email = "hawath.faisal@howzapp.com",
                hashedPassword = passwordEncoder.encode("Hafa@123"),
                profilePictureUrl = ""
            )
        )

        val riyas = userRepository.saveAndFlush(
            UserEntity(
                id = UserId.id,
                name = "Riyasdeen",
                email = "riyas.deen@howzapp.com",
                hashedPassword = passwordEncoder.encode("Riyas@123"),
                profilePictureUrl = ""
            )
        )

        newMessageUsecase.invoke(
            from = irsath.id,
            chatId = listOf(irsath.id, riyas.id).sorted().joinToString("__"),
            message = MessageDto.TextMessageDto("Good morning pa")
        )

        newMessageUsecase.invoke(
            from = riyas.id,
            chatId = listOf(irsath.id, riyas.id).sorted().joinToString("__"),
            message = MessageDto.TextMessageDto("Good morning da")
        )

        newMessageUsecase.invoke(
            from = riyas.id,
            chatId = listOf(irsath.id, riyas.id).sorted().joinToString("__"),
            message = MessageDto.TextMessageDto("Enna panra")
        )

        newMessageUsecase.invoke(
            from = irsath.id,
            chatId = listOf(irsath.id, riyas.id).sorted().joinToString("__"),
            message = MessageDto.TextMessageDto("Saptutu irukan.... Nee")
        )

        println("Messages Stored ..... Verify With Database")
    }
}

/*
*
select * from user_entity;
select * from chat_entity;
select * from one_to_one_chat_entity;
select * from group_chat_entity;
select * from chat_participants;
select * from text_message_entity;
select * from chat_message_status_entity;
select * from chat_message_receipts_entity;
*/