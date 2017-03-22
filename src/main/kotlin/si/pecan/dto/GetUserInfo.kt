package si.pecan.dto

import java.util.*

/**
 * Created by matjazpecan on 22/03/2017.
 */
data class GetUserInfoRequest(val user: String)
data class GetUserInfoResponse(val user: String, val id: UUID)