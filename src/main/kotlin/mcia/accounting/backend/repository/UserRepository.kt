package mcia.accounting.backend.repository

import mcia.accounting.backend.entity.AppUser
import java.util.*

interface UserRepository : SearchRepository<AppUser> {

    fun findByUsername(username: String): Optional<AppUser>

}
