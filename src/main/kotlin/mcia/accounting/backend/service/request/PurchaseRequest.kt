package mcia.accounting.backend.service.request

import java.math.BigDecimal
import java.util.*

data class PurchaseRequest(val id: Long = -1,
                           val item: String,
                           val code: String,
                           val amount: BigDecimal,
                           val comments: String,
                           val requestDate: Date,
                           val invoiceFile: String?,
                           val requestingEmployeeId: Long,
                           val requestingProjectId: Long,
                           val chargingProjectId: Long,
                           val stateId: Long,
                           val typeId: Long,
                           val supplierId: Long)
