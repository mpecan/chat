package si.pecan

import com.winterbe.expekt.should
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import si.pecan.Stubs.Companion.ANOTHER_VALID_USER
import si.pecan.Stubs.Companion.VALID_USER
import si.pecan.model.ChatRoom
import si.pecan.model.InstantMessage
import javax.transaction.Transactional

/**
 * Created by matjaz on 3/21/17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
open class RepositoryTests {


    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var chatRepository: ChatRoomRepository

    @Autowired
    lateinit var instantMessageRepository: InstantMessageRepository

    @Test
    fun testUserRepository() {
        val user = userRepository.save(VALID_USER)
        user.id.should.not.be.`null`
        userRepository.count().should.equal(1)
    }


    @Test
    fun testUserFound() {
        userRepository.save(VALID_USER)
        userRepository.findByUsername("testuser").should.not.be.`null`
    }


    @Test
    fun testCreateChatRoom() {
        val chatRoom = createChatRoom()
        chatRoom.created.should.not.be.`null`
    }

    @Test
    fun testCreateChat() {
        val chatRoom = createChatRoom()
        val user = userRepository.findByUsername("testuser") ?: throw Exception()

        val message = instantMessageRepository.save(InstantMessage().apply {
            room = chatRoom
            postedBy = user
            content = "some chat"
        })
        message.id.should.not.be.`null`
    }

    private fun createChatRoom(): ChatRoom {
        val users = userRepository.save(arrayListOf(VALID_USER, ANOTHER_VALID_USER)).toCollection(mutableListOf())
        val chatRoom = chatRepository.save(ChatRoom().apply {
            this.users = users
            this.createdBy = users.first()
        })
        return chatRoom
    }

}
