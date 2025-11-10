package com.techullurgy.howzapp.chats.infra.utils

@Suppress("ClassName")
object JPQLQueries {

    object FETCH_NEW_MESSAGES_FOR_USER {
        const val PARAM_MESSAGE_AFTER = "messagesAfter"
        const val PARAM_USER = "user"

//        const val QUERY = """
//            SELECT new com.techullurgy.howzapp.chats.infra.database.projections.ChatMessageProjection(m,s,r)
//            FROM ChatMessageEntity m
//            LEFT JOIN ChatMessageStatusEntity s ON s.message = m AND s.sender = :$PARAM_USER
//            LEFT JOIN ChatMessageReceiptsEntity r ON r.message = m AND r.user = :$PARAM_USER
//            WHERE
//                :$PARAM_USER MEMBER OF m.belongsToChat.participants
//                AND m.createdAt >= :$PARAM_MESSAGE_AFTER
//            ORDER BY m.createdAt DESC
//        """

        const val QUERY = """
            SELECT new com.techullurgy.howzapp.chats.infra.database.projections.ChatMessageProjection(m,s,r)
            FROM ChatMessageEntity m
            LEFT JOIN ChatMessageStatusEntity s ON s.message = m AND s.sender = :$PARAM_USER
            LEFT JOIN ChatMessageReceiptsEntity r ON r.message = m AND r.user = :$PARAM_USER
            WHERE 
                :$PARAM_USER MEMBER OF m.belongsToChat.participants 
                AND (
                    m.updatedAt >= :$PARAM_MESSAGE_AFTER
                    OR s.updatedAt >= :$PARAM_MESSAGE_AFTER
                    OR r.updatedAt >= :$PARAM_MESSAGE_AFTER
                )
            ORDER BY m.createdAt DESC
        """
    }


    object LOADING_SLICE_OF_MESSAGES_FOR_CHAT {
        const val PARAM_BEFORE_MESSAGE = "beforeMessage"
        const val PARAM_CHAT = "chat"
        const val PARAM_REQUESTER = "requester"

        const val QUERY = """
            SELECT new com.techullurgy.howzapp.chats.infra.database.projections.ChatMessageProjection(m,s,r)
            FROM ChatMessageEntity m
            JOIN m.belongsToChat c
            LEFT JOIN ChatMessageStatusEntity s ON s.message = m AND s.sender.id = :$PARAM_REQUESTER
            LEFT JOIN ChatMessageReceiptsEntity r ON r.message = m AND r.user.id = :$PARAM_REQUESTER
            WHERE c.id = :$PARAM_CHAT AND m.createdAt < (SELECT sm.createdAt FROM ChatMessageEntity sm WHERE sm.id = :$PARAM_BEFORE_MESSAGE)
            ORDER BY m.createdAt DESC
        """
    }

    object GET_MESSAGE_FOR_USER {
        const val PARAM_MESSAGE = "message"
        const val PARAM_REQUESTER = "requester"

        const val QUERY = """
            SELECT new com.techullurgy.howzapp.chats.infra.database.projections.ChatMessageProjection(m,s,r)
            FROM ChatMessageEntity m
            JOIN m.belongsToChat c
            LEFT JOIN ChatMessageStatusEntity s ON s.message = m AND s.sender.id = :$PARAM_REQUESTER
            LEFT JOIN ChatMessageReceiptsEntity r ON r.message = m AND r.user.id = :$PARAM_REQUESTER
            WHERE m.id = :$PARAM_MESSAGE
            ORDER BY m.createdAt DESC
        """
    }


    object FETCH_ONE_TO_ONE_COMMON_CHAT_IF_ANY {
        const val PARAM_MEMBER_1 = "member1"
        const val PARAM_MEMBER_2 = "member2"

        const val QUERY = """
            SELECT c FROM ChatEntity c JOIN c.participants
            WHERE
                type(c) = OneToOneChatEntity
                AND :$PARAM_MEMBER_1 MEMBER OF c.participants AND :$PARAM_MEMBER_2 MEMBER OF c.participants
        """
    }

    object GET_RECEIPTS_FOR_MESSAGE_FOR_USER {
        const val PARAM_USER_ID = "userId"
        const val PARAM_MESSAGE_ID = "messageId"

        const val QUERY = """
            SELECT r
            FROM ChatMessageReceiptsEntity r
            JOIN r.message m
            JOIN r.user u
            WHERE m.id = :$PARAM_MESSAGE_ID AND u.id = :$PARAM_USER_ID
        """
    }

    object UPDATE_RECEIPT_FOR_MESSAGE_AND_FOR_USER {
        const val PARAM_USER_ID = "userId"
        const val PARAM_MESSAGE_ID = "messageId"
        const val PARAM_RECEIPT = "receipt"
        const val PARAM_UPDATED_AT = "updatedAt"

        const val QUERY = """
            UPDATE ChatMessageReceiptsEntity
            SET receipt = :$PARAM_RECEIPT, updatedAt = :$PARAM_UPDATED_AT
            WHERE message.id = :$PARAM_MESSAGE_ID AND user.id = :$PARAM_USER_ID
        """
    }

    object CAN_UPDATE_DELIVERED_STATUS_OF_MESSAGE {
        const val PARAM_MESSAGE_ID = "messageId"
        const val PARAM_RECEIPT = "receipt"

        const val QUERY = """
            SELECT CASE WHEN COUNT(r) = 0 THEN true ELSE false END
            FROM ChatMessageReceiptsEntity r
            WHERE r.message.id = :$PARAM_MESSAGE_ID AND r.receipt < :$PARAM_RECEIPT
        """
    }

    object UPDATE_STATUS_OF_MESSAGE {
        const val QUERY = """
            UPDATE ChatMessageStatusEntity
            SET status = ?2, updatedAt = ?3
            WHERE message.id = ?1
        """
    }
}