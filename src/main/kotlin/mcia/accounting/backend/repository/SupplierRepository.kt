package mcia.accounting.backend.repository

import mcia.accounting.backend.entity.Supplier
import org.springframework.data.repository.CrudRepository

interface SupplierRepository : CrudRepository<Supplier, Long>
