package si.pecan.dto

import si.pecan.model.User
import java.util.*

/**
 * Created by matjazpecan on 22/03/2017.
 */
data class GetUserInfoRequest(val user: String)
data class GetUserInfoResponse(val user: String, val id: UUID)

fun User.toDto() = GetUserInfoResponse(this.username, this.id!!)