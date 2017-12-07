package mcia.accounting.backend.service.converter

import mcia.accounting.backend.entity.PurchaseType
import mcia.accounting.backend.service.request.PurchaseTypeRequest
import org.springframework.stereotype.Component

@Component
class PurchaseTypeConverter : RequestConverter<PurchaseType, PurchaseTypeRequest> {

    override fun toEntity(request: PurchaseTypeRequest, id: Long): PurchaseType =
            PurchaseType(
                    id = id,
                    name = request.name)

}
