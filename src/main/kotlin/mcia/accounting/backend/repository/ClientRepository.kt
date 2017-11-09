package mcia.accounting.backend.repository

import mcia.accounting.backend.entity.Client
import org.springframework.data.repository.CrudRepository

interface ClientRepository : CrudRepository<Client, Long>
