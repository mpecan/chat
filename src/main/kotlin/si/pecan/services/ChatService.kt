package si.pecan.services

import org.springframework.stereotype.Service
import si.pecan.*
import si.pecan.model.ChatRoom
import si.pecan.model.InstantMessage
import java.util.*

/**
 * Created by matjaz on 3/21/17.
 */
@Service
class ChatService(private val userRepository: UserRepository,
                  private val chatRoomRepository: ChatRoomRepository,
                  private val instantMessageRepository: InstantMessageRepository) {


    fun getOrCreateChat(initiatorUsername: String, targetUsername: String): UUID {
        val initiator = userRepository.findByUsername(initiatorUsername) ?: throw UserNotFound()
        val target = userRepository.findByUsername(targetUsername) ?: throw UserNotFound()
        val chatRoom = initiator.chatRooms.find { it.users.any { it == target } } ?: chatRoomRepository.save(ChatRoom().apply {
            users = arrayListOf(initiator, target)
            createdBy = initiator
        })

        return chatRoom.id!!
    }

    fun postMessage(username: String, chatId: UUID, messageContent: String): InstantMessage {
        val chat = chatRoomRepository.findOne(chatId) ?: throw ChatNotFound()
        val user = chat.users.find { it.username == username } ?: throw UserNotAllowedToAccessChat()
        return instantMessageRepository.save(InstantMessage().apply {
            room = chat
            content = messageContent
            postedBy = user
        })
    }
}