package si.pecan.services

import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import si.pecan.*
import si.pecan.dto.Message
import si.pecan.dto.toDto
import si.pecan.model.ChatRoom
import si.pecan.dto.ChatRoom as Dto
import si.pecan.model.InstantMessage
import si.pecan.model.User
import java.time.LocalDateTime
import java.util.*
import javax.transaction.Transactional
import kotlin.experimental.and


@Service
class ChatService(private val userRepository: UserRepository,
                  private val chatRoomRepository: ChatRoomRepository,
                  private val instantMessageRepository: InstantMessageRepository,
                  private val simpMessagingTemplate: SimpMessagingTemplate) {


    @Transactional
    fun getOrCreateChat(initiatorUsername: String, targetUsername: String): Dto {
        val target = userRepository.findByUsername(targetUsername) ?: throw UserNotFound()
        val initiator = userRepository.save(userRepository.findByUsername(initiatorUsername)?.apply {
            lastActive = LocalDateTime.now()
        } ?: throw UserNotFound())
        val chatRoom = initiator.chatRooms.find { it.users.any { it == target } } ?: chatRoomRepository.save(ChatRoom().apply {
            users = arrayListOf(initiator, target)
            createdBy = initiator
        })
        val theDto = chatRoom.toDto()
        chatRoom.users.forEach { simpMessagingTemplate.convertAndSend("/topic/rooms/${it.username}", theDto) }
        return theDto
    }

    fun ChatRoom.toDto(): si.pecan.dto.ChatRoom {
        return Dto(
                this.id!!,
                this.createdBy.toDto(),
                this.users.find { it != this.createdBy }!!.toDto(),
                this.messages.map(InstantMessage::toDto),
                this.created,
                if (this.messages.isEmpty()) null else this.messages.last().created
        )
    }

    @Transactional
    fun postMessage(username: String, chatId: UUID, messageContent: String): Message {
        val chat = chatRoomRepository.findOne(chatId) ?: throw ChatNotFound()
        val user = chat.users.find { it.username == username } ?: throw UserNotAllowedToAccessChat()
        return instantMessageRepository.save(InstantMessage().apply {
            room = chat
            content = messageContent
            postedBy = user

        }).toDto().apply {
            this.chatId = chatId
            this.postedByUser = userRepository.save(user.apply { lastActive = LocalDateTime.now()}).toDto()
        }
    }

    fun getAllRooms(username: String): List<Dto> = chatRoomRepository
            .findChatRoomIds(username)
            .map {
                var msb: Long = 0
                var lsb: Long = 0
                assert(it.size == 16) { "data must be 16 bytes in length" }
                for (i in 0..7)
                    msb = (msb shl 8) or (it[i].toLong() and 0xff)
                for (i in 8..15)
                    lsb = (lsb shl 8) or (it[i].toLong() and 0xff)

                UUID(msb, lsb)
            }
            .let { chatRoomRepository.findAll(it) }
            .map { it.toDto() }
}