package si.pecan.controller

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.winterbe.expekt.should
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.restdocs.JUnitRestDocumentation
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import si.pecan.dto.GetUserInfoRequest
import si.pecan.dto.GetUserInfoResponse
import javax.transaction.Transactional

/**
 * Created by matjaz on 3/21/17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
open class UserControllerTests {

    lateinit var mockMvc: MockMvc

    val mapper = jacksonObjectMapper().apply { propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE }

    @Autowired
    lateinit var context: WebApplicationContext


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
                .post("/api/user")
                .content(
                        mapper.writeValueAsString(
                                GetUserInfoRequest(user = "testUser")
                        )
                )
        ).andExpect {
            it.response.status.should.equal(200)
            val responseObject = mapper.readValue(it.response.contentAsString, GetUserInfoResponse::class.java)
            responseObject.id.should.not.be.`null`
        }
    }
}