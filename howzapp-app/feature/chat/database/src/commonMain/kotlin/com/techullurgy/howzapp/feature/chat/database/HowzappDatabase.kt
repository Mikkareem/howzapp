package com.techullurgy.howzapp.feature.chat.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.techullurgy.howzapp.feature.chat.database.converters.ChatTypeTypeConverter
import com.techullurgy.howzapp.feature.chat.database.converters.MessageTypeConverter
import com.techullurgy.howzapp.feature.chat.database.converters.OnlineStatusTypeConverter
import com.techullurgy.howzapp.feature.chat.database.converters.UploadStatusTypeConverter
import com.techullurgy.howzapp.feature.chat.database.dao.ChatDao
import com.techullurgy.howzapp.feature.chat.database.entities.ChatEntity
import com.techullurgy.howzapp.feature.chat.database.entities.ChatMessageEntity
import com.techullurgy.howzapp.feature.chat.database.entities.ChatMessageStatusEntity
import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantCrossRef
import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantEntity
import com.techullurgy.howzapp.feature.chat.database.views.ChatInfoView
import com.techullurgy.howzapp.feature.chat.database.views.ChatLastMessageView
import com.techullurgy.howzapp.feature.chat.database.views.ChatMessageView
import com.techullurgy.howzapp.feature.chat.database.views.ChatPendingMessagesView
import com.techullurgy.howzapp.feature.chat.database.views.ChatPendingUploadsSuccessView
import com.techullurgy.howzapp.feature.chat.database.views.ChatPendingUploadsTriggeredView


@Database(
    entities = [
        ChatEntity::class,
        ChatMessageEntity::class,
        ChatMessageStatusEntity::class,
        ChatParticipantEntity::class,
        ChatParticipantCrossRef::class,
    ],
    views = [
        ChatMessageView::class,
        ChatInfoView::class,
        ChatLastMessageView::class,
        ChatPendingMessagesView::class,
        ChatPendingUploadsSuccessView::class,
        ChatPendingUploadsTriggeredView::class
    ],
    version = 1
)
@TypeConverters(
    ChatTypeTypeConverter::class,
    MessageTypeConverter::class,
    OnlineStatusTypeConverter::class,
    UploadStatusTypeConverter::class
)
@ConstructedBy(HowzappDatabaseConstructor::class)
abstract class HowzappDatabase: RoomDatabase() {
    abstract val chatDao: ChatDao

    companion object {
        const val DB_NAME: String = "howzapp.db"
    }
}