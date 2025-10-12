package com.techullurgy.howzapp.chats.infra.utils

@Suppress("ClassName")
object JPQLQueries {

    object FETCH_NEW_MESSAGES_FOR_USER {
        const val QUERY = """
            SELECT m, s
            FROM ChatMessageEntity m
            JOIN m.belongsToChat c
            LEFT JOIN ChatMessageStatusEntity s ON s.message = m AND s.sender = :user
            WHERE :user MEMBER OF c.participants AND m.createdAt >= :lastSyncedTime
            ORDER BY m.createdAt DESC
        """

        const val PARAM_LAST_SYNCED_TIME = "lastSyncedTime"
        const val PARAM_USER = "user"
    }


    object LOADING_SLICE_OF_MESSAGES_FOR_CHAT {
        const val QUERY = """
            SELECT m, s
            FROM ChatMessageEntity m
            JOIN m.belongsToChat c
            LEFT JOIN ChatMessageStatusEntity s ON s.message = m AND s.sender = :requester
            WHERE c = :chat AND m.createdAt < :beforeCreatedAt
            ORDER BY m.createdAt DESC
        """

        const val PARAM_BEFORE_CREATED_AT = "beforeCreatedAt"
        const val PARAM_CHAT = "chat"
        const val PARAM_REQUESTER = "requester"
    }


    object FETCH_ONE_TO_ONE_COMMON_CHAT_IF_ANY {
        const val QUERY = """
            SELECT c FROM ChatEntity c JOIN c.participants
            WHERE
                type(c) = OneToOneChatEntity
                AND :member1 MEMBER OF c.participants AND :member2 MEMBER OF c.participants
        """

        const val PARAM_MEMBER_1 = "member1"
        const val PARAM_MEMBER_2 = "member2"
    }

    object GET_RECEIPTS_FOR_MESSAGE_FOR_USER {
        const val QUERY = """
            SELECT r
            FROM ChatMessageReceiptsEntity r
            JOIN r.message m
            JOIN r.user u
            WHERE m.id = :messageId AND u.id = :userId
        """

        const val PARAM_USER_ID = "userId"
        const val PARAM_MESSAGE_ID = "messageId"
    }
}