package mcia.accounting.backend.service.auth

import mcia.accounting.backend.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class CustomUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
                .orElseThrow { UsernameNotFoundException("username not found") }
        return User.builder()
                .username(user.username)
                .password(user.password)
                .disabled(!user.enabled)
                .roles(*user.roles.map { it.name }.toTypedArray())
                .build()
    }

}
