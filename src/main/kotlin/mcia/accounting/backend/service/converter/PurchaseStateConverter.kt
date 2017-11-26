package mcia.accounting.backend.service.converter

import mcia.accounting.backend.entity.PurchaseState
import mcia.accounting.backend.service.request.PurchaseStateRequest
import org.springframework.stereotype.Component

@Component
class PurchaseStateConverter : RequestConverter<PurchaseState, PurchaseStateRequest> {

    override fun toEntity(request: PurchaseStateRequest, id: Long): PurchaseState =
            PurchaseState(
                    id = id,
                    name = request.name)

}
