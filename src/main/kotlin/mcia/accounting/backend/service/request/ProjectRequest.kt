package mcia.accounting.backend.service.request

data class ProjectRequest(val id: Long = -1,
                          val name: String,
                          val description: String,
                          val code: String,
                          val finished: Boolean,
                          val managerId: Long,
                          val clientId: Long,
                          val typeId: Long)
