package mcia.accounting.backend.repository

import mcia.accounting.backend.entity.Purchase
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.PagingAndSortingRepository

interface PurchaseRepository : PagingAndSortingRepository<Purchase, Long>, JpaSpecificationExecutor<Purchase>
