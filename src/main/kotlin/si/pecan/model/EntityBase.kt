package si.pecan.model

import java.util.*
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass


@MappedSuperclass
open class EntityBase {


    @Id
    @GeneratedValue
    var id: UUID? = null

}