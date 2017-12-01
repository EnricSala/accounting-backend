package mcia.accounting.backend.repository

import mcia.accounting.backend.entity.AppUser
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository : CrudRepository<AppUser, Long> {

    fun findByUsername(username: String): Optional<AppUser>

}
