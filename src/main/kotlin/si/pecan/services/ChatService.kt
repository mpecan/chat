package si.pecan.services

import org.springframework.stereotype.Service
import si.pecan.model.InstantMessage
import java.util.*

/**
 * Created by matjaz on 3/21/17.
 */
@Service
class ChatService {
    fun createChat(initiatorUsername: String, targetUsername: String) : UUID {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun postMessage(username: String, chatId: UUID, content: String): InstantMessage {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}