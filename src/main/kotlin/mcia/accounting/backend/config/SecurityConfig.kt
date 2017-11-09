package mcia.accounting.backend.config

import org.springframework.boot.actuate.autoconfigure.security.EndpointRequest
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User

@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) {
        val admin = User.withUsername("admin").password("admin").roles("ADMIN", "USER")
        val user = User.withUsername("user").password("user").roles("USER")
        auth.inMemoryAuthentication().withUser(admin).withUser(user)
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
        //      .requestMatchers(StaticResourceRequest.toCommonLocations()).permitAll()
        //      .requestMatchers(EndpointRequest.to("status", "info")).permitAll()
        //      .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("USER")
    }

}
