package mcia.accounting.backend.service.request

data class ClientRequest(val name: String,
                         val acronym: String,
                         val typeId: Long)
