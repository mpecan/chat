package si.pecan.dto

import si.pecan.model.User as DBUser
import java.time.LocalDateTime
import java.util.*

/**
 * Created by m.pecan on 22/03/2017.
 */
data class User(val username: String, val id: UUID, val lastActive: LocalDateTime)


fun DBUser.toDto() = User(this.username, this.id!!, this.lastActive)