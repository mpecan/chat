package si.pecan.service

import com.winterbe.expekt.should
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import si.pecan.DuplicateUserException
import si.pecan.Stubs.Companion.VALID_USER
import si.pecan.model.User
import si.pecan.services.UserService
import javax.transaction.Transactional

/**
 * Created by matjaz on 3/21/17.
 */

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
class UserServiceTests {

    @Autowired
    lateinit var userService: UserService

    @Test
    fun testUserCreation() {
        val user:User = userService.create(User().apply {
            username = "newUser"
        })
        user.id.should.not.be.`null`
    }

    @Test(expected = DuplicateUserException::class)
    fun testUserUniqueness() {
        userService.create(VALID_USER)
        userService.create(VALID_USER)
    }

    
}