package si.pecan

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "User does not have access to chat.")
class UserNotAllowedToAccessChat : RuntimeException()

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User could not be found")
class UserNotFound : RuntimeException()

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Chat could not be found")
class ChatNotFound : RuntimeException()