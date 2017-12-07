package mcia.accounting.backend.service.converter

import mcia.accounting.backend.entity.Supplier
import mcia.accounting.backend.service.request.SupplierRequest
import org.springframework.stereotype.Component

@Component
class SupplierConverter : RequestConverter<Supplier, SupplierRequest> {

    override fun toEntity(request: SupplierRequest, id: Long): Supplier =
            Supplier(
                    id = id,
                    name = request.name)

}
