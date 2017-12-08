package mcia.accounting.backend.service.converter

import mcia.accounting.backend.entity.AppRole
import mcia.accounting.backend.entity.AppUser
import mcia.accounting.backend.repository.EmployeeRepository
import mcia.accounting.backend.repository.UserRepository
import mcia.accounting.backend.service.exception.InvalidRequestException
import mcia.accounting.backend.service.exception.ResourceNotFoundException
import mcia.accounting.backend.service.request.UserRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserConverter(private val userRepository: UserRepository,
                    private val employeeRepository: EmployeeRepository,
                    private val passwordEncoder: PasswordEncoder) : RequestConverter<AppUser, UserRequest> {

    override fun toEntity(request: UserRequest, id: Long): AppUser {
        val employee = employeeRepository.findById(request.employeeId)
                .orElseThrow { ResourceNotFoundException("employee id not found") }
        return AppUser(
                id = id,
                username = request.username,
                password = passwordOf(id),
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
            throw InvalidRequestException("unknown role: $name")
        }
        return names.map { toRole(it) }.toMutableSet()
    }

}
