package si.pecan.services

import org.springframework.stereotype.Service
import si.pecan.UserRepository
import si.pecan.model.User
import java.time.LocalDateTime

/**
 * Created by matjaz on 3/21/17.
 */
@Service
class UserService(private val userRepository: UserRepository) {
    fun getOrCreate(user: String): User =
            userRepository.findByUsername(user)
                    ?.let { userRepository
                            .save(it.apply { this.lastActive = LocalDateTime.now() })
                    }
                    ?: userRepository.save(User().apply { username = user })
    fun getAll() = userRepository.findAll().toCollection(arrayListOf())
}