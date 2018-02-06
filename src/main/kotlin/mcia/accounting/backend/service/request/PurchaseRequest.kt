package mcia.accounting.backend.service.request

import java.math.BigDecimal
import java.util.*

data class PurchaseRequest(val item: String,
                           val code: String,
                           val codeRP: String,
                           val codeLV: String,
                           val amount: BigDecimal,
                           val comments: String,
                           val requestDate: Date,
                           val requestingEmployeeId: Long,
                           val requestingProjectId: Long,
                           val chargingProjectId: Long,
                           val stateId: Long,
                           val typeId: Long,
                           val supplierId: Long)
