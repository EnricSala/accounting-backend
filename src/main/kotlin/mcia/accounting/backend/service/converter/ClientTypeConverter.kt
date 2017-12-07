package mcia.accounting.backend.service.converter

import mcia.accounting.backend.entity.ClientType
import mcia.accounting.backend.service.request.ClientTypeRequest
import org.springframework.stereotype.Component

@Component
class ClientTypeConverter : RequestConverter<ClientType, ClientTypeRequest> {

    override fun toEntity(request: ClientTypeRequest, id: Long): ClientType =
            ClientType(
                    id = id,
                    name = request.name)

}
