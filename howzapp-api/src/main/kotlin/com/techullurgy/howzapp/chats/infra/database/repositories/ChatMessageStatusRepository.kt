package com.techullurgy.howzapp.chats.infra.database.repositories

import com.techullurgy.howzapp.chats.infra.database.entities.ChatMessageStatusEntity
import com.techullurgy.howzapp.common.types.MessageStatusId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatMessageStatusRepository: JpaRepository<ChatMessageStatusEntity, MessageStatusId>