package si.pecan

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Created by matjaz on 3/21/17.
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "A user with the supplied username already exists.")
class DuplicateUserException: RuntimeException()