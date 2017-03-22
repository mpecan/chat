package si.pecan.dto

import si.pecan.model.InstantMessage
import java.time.LocalDateTime
import java.util.*


/**
 * Created by m.pecan on 22/03/2017.
 */
data class ChatRoom(val id: UUID, val initiator: User, val target: User, val messages: List<Message>, val created: LocalDateTime, val lastMessageTime: LocalDateTime?)

data class Message(val poster: String, val content: String, val created: LocalDateTime, val id: UUID, var chatId: UUID? = null)

fun InstantMessage.toDto() = Message(postedBy.username, content, created, this.id!!)