package si.pecan.model

import java.util.*
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

/**
 * Created by matjaz on 3/21/17.
 */
@MappedSuperclass
open class EntityBase {


    @Id
    @GeneratedValue
    var id: UUID? = null

}