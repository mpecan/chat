package si.pecan

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import si.pecan.model.ChatRoom
import si.pecan.model.InstantMessage
import si.pecan.model.User
import java.util.*

/**
 * Created by matjaz on 3/21/17.
 */
interface UserRepository : CrudRepository<User, UUID> {
    fun findByUsername(username: String): User?
    @Query("SELECT user FROM User user ORDER BY user.lastActive DESC")
    fun findUsersOrderByLastActiveDesc(): List<User>
}

interface ChatRoomRepository : CrudRepository<ChatRoom, UUID>
interface InstantMessageRepository : CrudRepository<InstantMessage, UUID>