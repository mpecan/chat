package si.pecan.dto

import java.util.*

/**
 * Created by m.pecan on 22/03/2017.
 */
data class PostMessageRequest(val username: String, val chatId: UUID, val content: String)
