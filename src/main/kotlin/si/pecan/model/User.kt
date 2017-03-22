package si.pecan.model

import java.time.LocalDateTime
import javax.persistence.*

/**
 * Created by matjaz on 3/18/17.
 */
@Entity
@Table(name = "users", indexes = arrayOf(
        Index(name = "users_username_unique", columnList = "username", unique = true)
))
class User : EntityBase() {

    @Column(length = 255)
    lateinit var username: String

    var active: Boolean = false

    var lastActive: LocalDateTime = LocalDateTime.now()

    @ManyToMany(mappedBy = "users")
    var chatRooms: List<ChatRoom> = listOf()

}