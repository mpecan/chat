package si.pecan.controller

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
class UserController(private val userService: UserService) {

    @RequestMapping(method = arrayOf(RequestMethod.POST))
    fun getUser(@RequestBody request: GetUserInfoRequest) = userService.getOrCreate(request.username).toDto()

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun getOtherUsers(@RequestParam("username") username: String) = userService.getAllOrdered().filter { it.username != username }.map(User::toDto)
}

