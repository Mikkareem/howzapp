package com.techullurgy.howzapp.chats.infra.database.repositories

import com.techullurgy.howzapp.chats.infra.database.entities.ChatEntity
import com.techullurgy.howzapp.chats.infra.database.entities.chats.OneToOneChatEntity
import com.techullurgy.howzapp.chats.infra.utils.JPQLQueries
import com.techullurgy.howzapp.common.types.ChatId
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository: JpaRepository<ChatEntity, ChatId> {

    @Query(JPQLQueries.FETCH_ONE_TO_ONE_COMMON_CHAT_IF_ANY.QUERY)
    fun findOneToOneChatForParticipants(
        @Param(JPQLQueries.FETCH_ONE_TO_ONE_COMMON_CHAT_IF_ANY.PARAM_MEMBER_1) member1: UserEntity,
        @Param(JPQLQueries.FETCH_ONE_TO_ONE_COMMON_CHAT_IF_ANY.PARAM_MEMBER_2) member2: UserEntity
    ): OneToOneChatEntity?
}