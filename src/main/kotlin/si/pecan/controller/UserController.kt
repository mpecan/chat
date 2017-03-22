package si.pecan.controller

import org.jetbrains.annotations.TestOnly
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import si.pecan.dto.GetUserInfoRequest
import si.pecan.dto.toDto
import si.pecan.services.UserService

/**
 * Created by matjaz on 3/21/17.
 */
@RestController
@RequestMapping("/api/username")
class UserController(private val userService: UserService) {

    @RequestMapping(method = arrayOf(RequestMethod.POST))
    fun getUser(@RequestBody request: GetUserInfoRequest) = userService.getOrCreate(request.username).toDto()
}

