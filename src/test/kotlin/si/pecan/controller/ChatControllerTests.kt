package si.pecan.controller

import com.winterbe.expekt.should
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import si.pecan.dto.ChatRoom
import si.pecan.services.ChatService
import javax.transaction.Transactional

/**
 * Created by m.pecan on 22/03/2017.
 */

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
open class ChatControllerTests: ControllerTestBase() {

    @Autowired
    lateinit var chatService: ChatService


    @Test
    fun testGetChatWithUser(){
        val initiator = "user"
        val target = "target"

        val users = arrayOf(initiator, target).map { userService.getOrCreate(it) }

        mockMvc.perform(MockMvcRequestBuilders.get("/api/chat").param("username", initiator).param("partner", target))
                .andExpect(status().isOk)
                .andExpect {
                    mapper.readValue(it.response.contentAsString, ChatRoom::class.java).should.be.an.instanceof(ChatRoom::class.java)
                }

    }





}