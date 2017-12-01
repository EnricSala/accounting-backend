package mcia.accounting.backend.config

import mcia.accounting.backend.repository.UserRepository
import mcia.accounting.backend.service.auth.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class SecurityConfig(private val userRepository: UserRepository) : WebSecurityConfigurerAdapter() {

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun customUserDetailsService() = CustomUserDetailsService(userRepository)

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
                .userDetailsService(customUserDetailsService())
                .passwordEncoder(passwordEncoder())
    }

    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/js/**", "/css/**", "/img/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/user/self").hasRole("USER")
                    .antMatchers(HttpMethod.POST, "/api/user/self/password").hasRole("USER")
                    .antMatchers("/api/user/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.POST, "/api/purchase/**").hasRole("USER")
                    .antMatchers(HttpMethod.PUT, "/api/purchase/**").hasRole("USER")
                    .antMatchers(HttpMethod.GET, "/api/**").hasRole("USER")
                    .antMatchers("/api/**").hasRole("MANAGER")
                    .and()
                .csrf()
                    .disable()
                .httpBasic()
    }

}
