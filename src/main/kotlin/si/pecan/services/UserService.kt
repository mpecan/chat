package si.pecan.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import si.pecan.UserRepository
import si.pecan.model.User
import java.time.LocalDateTime


@Service
class UserService(private val userRepository: UserRepository) {
    @Transactional
    fun getOrCreate(user: String): User =
            userRepository.findByUsername(user)
                    ?.let {
                        userRepository
                                .save(it.apply { this.lastActive = LocalDateTime.now() })
                    }
                    ?: userRepository.save(User().apply { username = user })

    fun getAllOrdered() = userRepository.findUsersOrderByLastActiveDesc()
}