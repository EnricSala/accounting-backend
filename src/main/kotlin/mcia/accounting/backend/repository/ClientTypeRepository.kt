package mcia.accounting.backend.repository

import mcia.accounting.backend.entity.ClientType
import org.springframework.data.repository.CrudRepository

interface ClientTypeRepository : CrudRepository<ClientType, Long>
