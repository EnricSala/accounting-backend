package mcia.accounting.backend.service.auth

import mcia.accounting.backend.entity.AppRole
import mcia.accounting.backend.entity.AppUser
import mcia.accounting.backend.repository.EmployeeRepository
import mcia.accounting.backend.repository.UserRepository
import mcia.accounting.backend.service.exception.InvalidRequestException
import mcia.accounting.backend.service.exception.ResourceNotFoundException
import mcia.accounting.backend.service.exception.UnauthorizedException
import mcia.accounting.backend.service.request.UserRequest
import mcia.accounting.backend.utils.loggerOf
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserService(private val userRepository: UserRepository,
                  private val employeeRepository: EmployeeRepository,
                  private val passwordEncoder: PasswordEncoder) {

    @Transactional(readOnly = true)
    fun findAll(): Iterable<AppUser> = userRepository.findAll()

    @Transactional(readOnly = true)
    fun findById(id: Long): AppUser = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("user id not found") }

    @Transactional(readOnly = true)
    fun findByUsername(username: String): AppUser = userRepository.findByUsername(username)
            .orElseThrow { ResourceNotFoundException("username not found") }

    @Transactional
    fun create(request: UserRequest): AppUser =
            userRepository.save(toUser(request))
                    .also { log.info("Created {}", it) }

    @Transactional
    fun update(id: Long, request: UserRequest): AppUser = when {
        userRepository.existsById(id) ->
            userRepository.save(toUser(request, id))
                    .also { log.info("Updated {}", it) }
        else -> throw ResourceNotFoundException("user id not found")
    }

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

    @Transactional
    fun deleteById(id: Long) = userRepository.deleteById(id)
            .also { log.info("Deleted AppUser(id={})", id) }

    private fun toUser(request: UserRequest, userId: Long = -1): AppUser {
        val employee = employeeRepository.findById(request.employeeId)
                .orElseThrow { ResourceNotFoundException("employee id not found") }
        return AppUser(
                id = userId,
                username = request.username,
                password = passwordOf(userId),
                enabled = request.enabled,
                employee = employee,
                roles = toRoles(request.roles))
    }

    private fun passwordOf(userId: Long): String = when {
        userId < 0 -> passwordEncoder.encode(UUID.randomUUID().toString())
        else -> userRepository.findById(userId).map { it.password }
                .orElseThrow { ResourceNotFoundException("user id not found") }
    }

    private fun toRoles(names: Set<String>): MutableSet<AppRole> {
        fun toRole(name: String) = try {
            AppRole.valueOf(name)
        } catch (e: IllegalArgumentException) {
            throw InvalidRequestException("unkown role: $name")
        }
        return names.map { toRole(it) }.toMutableSet()
    }

    companion object {
        private val log = loggerOf(UserService::class)
    }

}
