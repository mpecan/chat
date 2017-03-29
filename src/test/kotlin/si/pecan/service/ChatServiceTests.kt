package si.pecan.service

import com.winterbe.expekt.should
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import si.pecan.UserNotAllowedToAccessChat
import si.pecan.dto.ChatRoom
import si.pecan.dto.Message
import si.pecan.model.InstantMessage
import si.pecan.services.ChatService
import si.pecan.services.UserService
import java.util.*
import javax.transaction.Transactional


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
open class ChatServiceTests {

    companion object {
        val USER1 = "my_username"
        val USER2 = "second_user"
        val UNAUTHORIZED_USER = "third_user"
    }

    @Autowired
    lateinit var chatService: ChatService

    @Autowired
    lateinit var userService: UserService

    @Test
    fun createChat() {
        val chatId = newChat()
        chatId.should.not.be.`null`
    }

    fun newChat(): ChatRoom {
        createUsers()
        return chatService.getOrCreateChat(USER1, USER2)
    }

    private fun createUsers() {
        userService.getOrCreate(USER1)
        userService.getOrCreate(USER2)
        userService.getOrCreate(UNAUTHORIZED_USER)
    }

    @Test
    fun postToChat() {
        val chat: ChatRoom = newChat()
        val message: Message = chatService.postMessage(USER1, chat.id, "Message content here")
        message.id.should.not.be.`null`
    }

    @Test(expected = UserNotAllowedToAccessChat::class)
    fun postToChatWrongUser() {
        val chat: ChatRoom = newChat()
        chatService.postMessage(UNAUTHORIZED_USER, chat.id, "Message content here")
    }


}