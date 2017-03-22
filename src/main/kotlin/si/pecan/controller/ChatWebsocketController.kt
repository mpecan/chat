package si.pecan.controller

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ResponseBody
import si.pecan.dto.PostMessageRequest
import si.pecan.services.ChatService

/**
 * Created by m.pecan on 22/03/2017.
 */
@Controller
class ChatWebsocketController(private val chatService: ChatService) {

    @MessageMapping("/post/{roomId}")
    @ResponseBody
    fun receiveMessage(@Payload messageRequest: PostMessageRequest) = chatService.postMessage(messageRequest.username, messageRequest.chatId, messageRequest.content)
}