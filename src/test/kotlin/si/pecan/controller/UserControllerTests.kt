package si.pecan.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.winterbe.expekt.should
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.JUnitRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import si.pecan.dto.GetUserInfoRequest
import si.pecan.dto.User
import si.pecan.services.UserService
import javax.transaction.Transactional


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
open class UserControllerTests : ControllerTestBase() {

    @Test
    fun getUser() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/user").contentType(MediaType.APPLICATION_JSON)
                .content(
                        mapper.writeValueAsString(
                                GetUserInfoRequest(username = "testUser")
                        )
                )
        ).andExpect(status().isOk)
                .andExpect {
                    mapper.readValue(it.response.contentAsString, User::class.java).should.be.an.instanceof(User::class.java)
                }
                .andDo(document("get username response",
                        requestFields(arrayListOf(
                                fieldWithPath("username").type(JsonFieldType.STRING).description("the username to be found")
                        )),
                        responseFields(
                                fieldWithPath("username").type(JsonFieldType.STRING).description("the username found"),
                                fieldWithPath("id").type(JsonFieldType.STRING).description("the id of the username found"),
                                fieldWithPath("last_active").type(JsonFieldType.STRING).description("the last active time of the username found")
                        ))
                )
    }

    @Test
    fun getAllUsers() {
        val users = arrayOf("username", "another", "yet_another").map { userService.getOrCreate(it) }

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user").param("username", "username"))
                .andExpect(status().isOk)
                .andExpect {
                    val result: List<User> = mapper.readValue(it.response.contentAsString, object : TypeReference<List<User>>() {})
                    result.size.should.be.above(1)
                    result.forEach { user ->
                        users.any { it.username == user.username }.should.equal(true)
                    }
                }
    }

    @Test
    fun usersShouldBeInOrder() {
        val usernames = arrayOf("username", "another", "yet_another", "last")
        usernames.map {
            Thread.sleep(200)
            userService.getOrCreate(it)
        }

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user").param("username", "username"))
                .andExpect(status().isOk)
                .andExpect {
                    val result: List<User> = mapper.readValue(it.response.contentAsString, object : TypeReference<List<User>>() {})
                    result.size.should.be.above(1)
                    // First user is skipped as it is the requesting user
                    // Order should be reversed from the insert order
                    result[0].username.should.equal(usernames[3])
                    result[1].username.should.equal(usernames[2])
                    result[2].username.should.equal(usernames[1])
                }
    }
}
