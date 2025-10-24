package com.techullurgy.howzapp.feature.chat.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.techullurgy.howzapp.feature.chat.database.converters.MessageTypeConverter
import com.techullurgy.howzapp.feature.chat.database.converters.PendingMessageTypeConverter
import com.techullurgy.howzapp.feature.chat.database.converters.ReceiptTypeConverter
import com.techullurgy.howzapp.feature.chat.database.converters.UploadStatusTypeConverter
import com.techullurgy.howzapp.feature.chat.database.dao.ChatDao
import com.techullurgy.howzapp.feature.chat.database.dao.DirectChatDao
import com.techullurgy.howzapp.feature.chat.database.dao.GroupChatDao
import com.techullurgy.howzapp.feature.chat.database.dao.MessageDao
import com.techullurgy.howzapp.feature.chat.database.dao.OnlineUsersDao
import com.techullurgy.howzapp.feature.chat.database.dao.ParticipantsCrossRefDao
import com.techullurgy.howzapp.feature.chat.database.dao.ParticipantsDao
import com.techullurgy.howzapp.feature.chat.database.dao.PendingMessageDao
import com.techullurgy.howzapp.feature.chat.database.dao.PendingReceiptsDao
import com.techullurgy.howzapp.feature.chat.database.dao.ReceiverStatusDao
import com.techullurgy.howzapp.feature.chat.database.dao.SenderStatusDao
import com.techullurgy.howzapp.feature.chat.database.dao.StatusDao
import com.techullurgy.howzapp.feature.chat.database.dao.UploadablePendingMessageDao
import com.techullurgy.howzapp.feature.chat.database.entities.ChatEntity
import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantCrossRef
import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantEntity
import com.techullurgy.howzapp.feature.chat.database.entities.DirectChatEntity
import com.techullurgy.howzapp.feature.chat.database.entities.GroupChatEntity
import com.techullurgy.howzapp.feature.chat.database.entities.MessageEntity
import com.techullurgy.howzapp.feature.chat.database.entities.OnlineUsersEntity
import com.techullurgy.howzapp.feature.chat.database.entities.PendingMessageEntity
import com.techullurgy.howzapp.feature.chat.database.entities.PendingReceiptsEntity
import com.techullurgy.howzapp.feature.chat.database.entities.ReceiverStatusEntity
import com.techullurgy.howzapp.feature.chat.database.entities.SenderStatusEntity
import com.techullurgy.howzapp.feature.chat.database.entities.StatusEntity
import com.techullurgy.howzapp.feature.chat.database.entities.UploadablePendingMessageEntity

@Database(
    entities = [
        ChatEntity::class,
        DirectChatEntity::class,
        GroupChatEntity::class,
        PendingMessageEntity::class,
        UploadablePendingMessageEntity::class,
        MessageEntity::class,
        StatusEntity::class,
        SenderStatusEntity::class,
        ReceiverStatusEntity::class,
        ChatParticipantEntity::class,
        ChatParticipantCrossRef::class,
        PendingReceiptsEntity::class,
        OnlineUsersEntity::class
    ],
    version = 1
)
@TypeConverters(
    MessageTypeConverter::class,
    UploadStatusTypeConverter::class,
    PendingMessageTypeConverter::class,
    ReceiptTypeConverter::class
)
@ConstructedBy(HowzappDatabaseConstructor::class)
abstract class HowzappDatabase: RoomDatabase() {
    abstract val chatDao: ChatDao
    abstract val directChatDao: DirectChatDao
    abstract val groupChatDao: GroupChatDao
    abstract val messageDao: MessageDao
    abstract val statusDao: StatusDao
    abstract val senderStatusDao: SenderStatusDao
    abstract val receiverStatusDao: ReceiverStatusDao
    abstract val participantsDao: ParticipantsDao
    abstract val participantsCrossRefDao: ParticipantsCrossRefDao
    abstract val pendingMessageDao: PendingMessageDao
    abstract val uploadablePendingMessageDao: UploadablePendingMessageDao
    abstract val pendingReceiptsDao: PendingReceiptsDao
    abstract val onlineUsersDao: OnlineUsersDao

    companion object {
        const val DB_NAME: String = "howzapp.db"
    }
}