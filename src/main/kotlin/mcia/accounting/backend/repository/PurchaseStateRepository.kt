package mcia.accounting.backend.repository

import mcia.accounting.backend.entity.PurchaseState
import org.springframework.data.repository.CrudRepository

interface PurchaseStateRepository : CrudRepository<PurchaseState, Long>
