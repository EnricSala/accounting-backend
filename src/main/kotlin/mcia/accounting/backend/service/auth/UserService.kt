package mcia.accounting.backend.service.auth

import mcia.accounting.backend.entity.AppUser
import mcia.accounting.backend.repository.UserRepository
import mcia.accounting.backend.service.BaseService
import mcia.accounting.backend.service.converter.UserConverter
import mcia.accounting.backend.service.exception.ResourceNotFoundException
import mcia.accounting.backend.service.exception.UnauthorizedException
import mcia.accounting.backend.service.request.UserRequest
import org.springframework.security.core.Authentication
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
    fun selfPasswordChange(auth: Authentication, newPassword: String) {
        val user = userRepository.findByUsername(auth.name)
                .orElseThrow { ResourceNotFoundException("username not found") }
                .apply { password = passwordEncoder.encode(newPassword) }
        userRepository.save(user)
        log.info("Self changed password of {}", user)
    }

    @Transactional
    fun forcePasswordChange(id: Long, auth: Authentication, newPassword: String) {
        if (!auth.authorities.map { it.authority }.contains("ROLE_ADMIN"))
            throw UnauthorizedException("not authorized to change passwords")
        val user = userRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("user id not found") }
                .apply { password = passwordEncoder.encode(newPassword) }
        userRepository.save(user)
        log.info("Admin changed password of {}", user)
    }

}
