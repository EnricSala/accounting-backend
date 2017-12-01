package mcia.accounting.backend.service.request

data class UserRequest(val username: String,
                       val enabled: Boolean,
                       val employeeId: Long,
                       val roles: Set<String>)
