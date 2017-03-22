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

/**
 * Created by matjaz on 3/21/17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
open class UserControllerTests {

    lateinit var mockMvc: MockMvc

    val mapper = jacksonObjectMapper().apply {
        propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        registerModule(JavaTimeModule())
    }

    @Autowired
    lateinit var context: WebApplicationContext

    @Autowired
    lateinit var userService: UserService

    @get:Rule
    var restDocumentation = JUnitRestDocumentation("build/generated-snippets")

    @Before
    fun setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply<DefaultMockMvcBuilder>(documentationConfiguration(this.restDocumentation))
                .build()
    }

    @Test
    fun getUser() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/username").contentType(MediaType.APPLICATION_JSON)
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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/username").param("username", "username"))
                .andExpect(status().isOk)
                .andExpect {
                    val result: List<User> = mapper.readValue(it.response.contentAsString, object : TypeReference<List<User>>(){})
                    result.size.should.be.above(1)
                }
    }
}
