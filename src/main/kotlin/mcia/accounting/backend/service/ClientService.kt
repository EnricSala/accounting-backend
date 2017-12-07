package mcia.accounting.backend.service

import mcia.accounting.backend.entity.Client
import mcia.accounting.backend.repository.ClientRepository
import mcia.accounting.backend.service.converter.ClientConverter
import mcia.accounting.backend.service.request.ClientRequest
import org.springframework.stereotype.Service

@Service
class ClientService(repository: ClientRepository, converter: ClientConverter) :
        BaseService<Client, ClientRequest>(Client.RESOURCE, repository, converter)
