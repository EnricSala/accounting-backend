package mcia.accounting.backend.service

import mcia.accounting.backend.entity.ClientType
import mcia.accounting.backend.repository.ClientTypeRepository
import mcia.accounting.backend.service.converter.ClientTypeConverter
import mcia.accounting.backend.service.request.ClientTypeRequest
import org.springframework.stereotype.Service

@Service
class ClientTypeService(repository: ClientTypeRepository, converter: ClientTypeConverter) :
        BaseService<ClientType, ClientTypeRequest>(ClientType.RESOURCE, repository, converter)
