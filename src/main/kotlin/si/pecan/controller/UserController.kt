package si.pecan.controller

import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.*
import si.pecan.dto.GetUserInfoRequest
import si.pecan.dto.toDto
import si.pecan.model.User
import si.pecan.services.UserService

/**
 * Created by matjaz on 3/21/17.
 */
@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService, private val simpMessagingTemplate: SimpMessagingTemplate) {



    @RequestMapping(method = arrayOf(RequestMethod.POST))
    fun getUser(@RequestBody request: GetUserInfoRequest) = userService.getOrCreate(request.username).toDto().apply {
        simpMessagingTemplate.convertAndSend("/topic/users", this)
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun getOtherUsers() = userService.getAllOrdered().map(User::toDto)
}

