package si.pecan.dto

import si.pecan.model.InstantMessage
import java.time.LocalDateTime
import java.util.*


/**
 * Created by m.pecan on 22/03/2017.
 */
data class ChatRoom(val id: UUID, val users: List<User>, val messages: List<Message>)

data class Message(val poster: String, val content: String, val created: LocalDateTime)

fun InstantMessage.toDto() = Message(postedBy.username, content, created)