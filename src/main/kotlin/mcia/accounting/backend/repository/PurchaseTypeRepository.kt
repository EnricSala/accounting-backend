package mcia.accounting.backend.repository

import mcia.accounting.backend.entity.PurchaseType
import org.springframework.data.repository.CrudRepository

interface PurchaseTypeRepository : CrudRepository<PurchaseType, Long>
