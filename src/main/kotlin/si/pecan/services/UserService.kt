package si.pecan.services

import org.springframework.stereotype.Service
import si.pecan.UserRepository
import si.pecan.model.User

/**
 * Created by matjaz on 3/21/17.
 */
@Service
class UserService(private val userRepository: UserRepository) {
    fun getOrCreate(user: String): User = userRepository.findByUsername(user) ?: userRepository.save(User().apply { username = user })
}