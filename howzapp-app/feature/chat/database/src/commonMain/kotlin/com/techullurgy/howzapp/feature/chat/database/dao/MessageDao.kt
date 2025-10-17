package com.techullurgy.howzapp.feature.chat.database.dao

import androidx.room.Dao
import com.techullurgy.howzapp.feature.chat.database.entities.MessageEntity

@Dao
interface MessageDao: BaseDao<MessageEntity>