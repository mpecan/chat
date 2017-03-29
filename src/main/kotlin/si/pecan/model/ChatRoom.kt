package si.pecan.model

import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "chat_rooms")
class ChatRoom : EntityBase() {

    var created: LocalDateTime = LocalDateTime.now()

    @JoinTable(name = "chat_room_user")
    @ManyToMany
    var users: List<User> = listOf()

    @ManyToOne
    lateinit var createdBy: User

    @OneToMany(mappedBy = "room")
    var messages: List<InstantMessage> = listOf()

}