package si.pecan.model

import java.time.LocalDateTime
import javax.persistence.*

/**
 * Created by matjaz on 3/18/17.
 */
@Entity
@Table(name = "chat_rooms")
class ChatRoom : EntityBase() {

    var created: LocalDateTime = LocalDateTime.now()

    @JoinTable(name = "chat_room_user")
    @ManyToMany
    var users: List<User> = listOf()

    @ManyToOne
    lateinit var createdBy: User

}