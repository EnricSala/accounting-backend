package mcia.accounting.backend.service.auth

import mcia.accounting.backend.entity.AppUser
import mcia.accounting.backend.repository.UserRepository
import mcia.accounting.backend.service.BaseService
import mcia.accounting.backend.service.converter.UserConverter
import mcia.accounting.backend.service.exception.ResourceNotFoundException
import mcia.accounting.backend.service.request.UserRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val userRepository: UserRepository,
                  private val passwordEncoder: PasswordEncoder,
                  converter: UserConverter) :
        BaseService<AppUser, UserRequest>(AppUser.RESOURCE, userRepository, converter) {

    @Transactional(readOnly = true)
    fun findByUsername(username: String): AppUser = userRepository.findByUsername(username)
            .orElseThrow { ResourceNotFoundException("username not found") }

    @Transactional
    fun selfPasswordChange(username: String, newPassword: String) {
        val user = findByUsername(username)
        user.password = passwordEncoder.encode(newPassword)
        userRepository.save(user)
        log.info("Self password change of {}", user)
    }

    @Transactional
    fun forcePasswordChange(userId: Long, newPassword: String) {
        val user = findById(userId)
        user.password = passwordEncoder.encode(newPassword)
        userRepository.save(user)
        log.info("Forced password change of {}", user)
    }

}
