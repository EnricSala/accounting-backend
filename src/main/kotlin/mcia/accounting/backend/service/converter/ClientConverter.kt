package mcia.accounting.backend.service.converter

import mcia.accounting.backend.entity.Client
import mcia.accounting.backend.repository.ClientTypeRepository
import mcia.accounting.backend.service.exception.ResourceNotFoundException
import mcia.accounting.backend.service.request.ClientRequest
import org.springframework.stereotype.Component

@Component
class ClientConverter(private val clientTypeRepository: ClientTypeRepository) : RequestConverter<Client, ClientRequest> {

    override fun toEntity(request: ClientRequest, id: Long): Client {
        val type = clientTypeRepository.findById(request.typeId)
                .orElseThrow { ResourceNotFoundException("client-type id not found") }
        return Client(
                id = id,
                name = request.name,
                acronym = request.acronym,
                type = type)
    }

}
