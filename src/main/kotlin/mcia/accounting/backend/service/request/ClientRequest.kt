package mcia.accounting.backend.service.request

data class ClientRequest(val id: Long = -1,
                         val name: String,
                         val acronym: String,
                         val typeId: Long)
