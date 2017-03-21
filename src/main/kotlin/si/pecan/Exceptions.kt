package si.pecan

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Created by matjaz on 3/21/17.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "User does not have access to chat.")
class UserNotAllowedToAccessChat: RuntimeException()