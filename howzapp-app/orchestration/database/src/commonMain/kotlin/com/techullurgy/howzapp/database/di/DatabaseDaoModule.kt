package com.techullurgy.howzapp.database.di

import com.techullurgy.howzapp.database.HowzappDatabase
import com.techullurgy.howzapp.features.chats.database.dao.ChatDao
import com.techullurgy.howzapp.features.chats.database.dao.DirectChatDao
import com.techullurgy.howzapp.features.chats.database.dao.GroupChatDao
import com.techullurgy.howzapp.features.chats.database.dao.MessageDao
import com.techullurgy.howzapp.features.chats.database.dao.OnlineUsersDao
import com.techullurgy.howzapp.features.chats.database.dao.ParticipantsCrossRefDao
import com.techullurgy.howzapp.features.chats.database.dao.ParticipantsDao
import com.techullurgy.howzapp.features.chats.database.dao.PendingMessageDao
import com.techullurgy.howzapp.features.chats.database.dao.PendingReceiptsDao
import com.techullurgy.howzapp.features.chats.database.dao.ReceiverStatusDao
import com.techullurgy.howzapp.features.chats.database.dao.SenderStatusDao
import com.techullurgy.howzapp.features.chats.database.dao.StatusDao
import com.techullurgy.howzapp.features.chats.database.dao.UploadablePendingMessageDao
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
internal class DatabaseDaoModule {
    @Single
    fun provideChatDao(db: HowzappDatabase): ChatDao = db.chatDao
    @Single
    fun provideDirectChatDao(db: HowzappDatabase): DirectChatDao = db.directChatDao
    @Single
    fun provideGroupChatDao(db: HowzappDatabase): GroupChatDao = db.groupChatDao
    @Single
    fun provideMessageDao(db: HowzappDatabase): MessageDao = db.messageDao
    @Single
    fun provideStatusDao(db: HowzappDatabase): StatusDao = db.statusDao
    @Single
    fun provideSenderStatusDao(db: HowzappDatabase): SenderStatusDao = db.senderStatusDao
    @Single
    fun provideReceiverStatusDao(db: HowzappDatabase): ReceiverStatusDao = db.receiverStatusDao
    @Single
    fun provideParticipantsDao(db: HowzappDatabase): ParticipantsDao = db.participantsDao
    @Single
    fun provideParticipantsCrossRefDao(db: HowzappDatabase): ParticipantsCrossRefDao =
        db.participantsCrossRefDao

    @Single
    fun providePendingMessageDao(db: HowzappDatabase): PendingMessageDao = db.pendingMessageDao
    @Single
    fun provideUploadablePendingMessageDao(db: HowzappDatabase): UploadablePendingMessageDao =
        db.uploadablePendingMessageDao

    @Single
    fun providePendingReceiptsDao(db: HowzappDatabase): PendingReceiptsDao = db.pendingReceiptsDao
    @Single
    fun provideOnlineUsersDao(db: HowzappDatabase): OnlineUsersDao = db.onlineUsersDao
}