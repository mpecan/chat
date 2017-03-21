package si.pecan.service

import com.winterbe.expekt.should
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import si.pecan.UserNotAllowedToAccessChat
import si.pecan.model.InstantMessage
import si.pecan.services.ChatService
import si.pecan.services.UserService
import java.util.*
import javax.transaction.Transactional

/**
 * Created by matjaz on 3/21/17.
 */
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
    fun createChat(): UUID {
        createUsers()
        val chatId: UUID? = chatService.createChat(USER1, USER2)
        chatId.should.not.be.`null`
        return chatId!!
    }

    private fun createUsers() {
        userService.getOrCreate(USER1)
        userService.getOrCreate(USER2)
        userService.getOrCreate(UNAUTHORIZED_USER)
    }

    @Test
    fun postToChat() {
        val chatId = createChat()
        val message: InstantMessage = chatService.postMessage(USER1, chatId, "Message content here")
        message.id.should.not.be.`null`
    }

    @Test(expected = UserNotAllowedToAccessChat::class)
    fun postToChatWrongUser() {
        val chatId: UUID = createChat()
        chatService.postMessage(UNAUTHORIZED_USER, chatId, "Message content here")
    }


}