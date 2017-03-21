package si.pecan.model

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table

/**
 * Created by matjaz on 3/18/17.
 */
@Entity
@Table(name="instant_messages")
class InstantMessage : EntityBase() {

    @ManyToOne
    lateinit var room: ChatRoom

    @ManyToOne
    lateinit var postedBy: User

    var content: String = ""

    var created = LocalDateTime.now()

}