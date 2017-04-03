package si.pecan

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import si.pecan.model.ChatRoom
import si.pecan.model.InstantMessage
import si.pecan.model.User
import java.util.*


interface UserRepository : CrudRepository<User, UUID> {
    fun findByUsername(username: String): User?
    @Query("SELECT user FROM User user ORDER BY user.lastActive DESC")
    fun findUsersOrderByLastActiveDesc(): List<User>
}

interface ChatRoomRepository : CrudRepository<ChatRoom, UUID> {

    @Query("SELECT room.id FROM chat_rooms AS room " +
            "JOIN chat_room_user as room_user ON room.id = room_user.chat_rooms_id " +
            "JOIN users AS user ON room_user.users_id = user.id WHERE user.username = ?", nativeQuery = true)
    fun findChatRoomIds(username: String): List<ByteArray>
}

interface InstantMessageRepository : CrudRepository<InstantMessage, UUID>