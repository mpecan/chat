package si.pecan.controller

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.*
import si.pecan.dto.PostMessageRequest
import si.pecan.services.ChatService


@RestController
@RequestMapping("/api/chat")
class ChatController(private val chatService: ChatService) {

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun getChat(@RequestParam username: String, @RequestParam partner: String) = chatService.getOrCreateChat(username, partner)


    @RequestMapping(value = "/all", method = arrayOf(RequestMethod.GET))
    fun getAllRoomsForUser(@RequestParam username: String) = chatService.getAllRooms(username)

    @RequestMapping(value = "/message", method = arrayOf(RequestMethod.POST))
    fun sendMessage(@RequestBody messageRequest: PostMessageRequest) = chatService.postMessage(messageRequest.username, messageRequest.chatId, messageRequest.content)

}