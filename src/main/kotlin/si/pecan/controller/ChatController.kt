package si.pecan.controller

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import si.pecan.services.ChatService

/**
 * Created by matjaz on 3/16/17.
 */
@RestController
@RequestMapping("/api/chat")
class ChatController(private val chatService: ChatService) {

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun getChat(@RequestParam username: String, @RequestParam partner: String) = chatService.getOrCreateChat(username, partner)


}