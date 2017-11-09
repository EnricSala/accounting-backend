package mcia.accounting.backend.service

import mcia.accounting.backend.entity.Client
import mcia.accounting.backend.repository.ClientRepository
import mcia.accounting.backend.repository.ClientTypeRepository
import mcia.accounting.backend.service.request.ClientRequest
import mcia.accounting.backend.utils.loggerOf
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ClientService(private val clientRepository: ClientRepository,
                    private val clientTypeRepository: ClientTypeRepository) {

    @Transactional(readOnly = true)
    fun findAll(): Iterable<Client> = clientRepository.findAll()

    @Transactional(readOnly = true)
    fun findById(id: Long): Client = clientRepository.findById(id)
            .orElseThrow { RuntimeException("client id not found") }

    @Transactional
    fun create(request: ClientRequest): Client {
        if (request.id < 0)
            throw RuntimeException("id must not be set on create")
        return clientRepository.save(toClient(request))
                .also { log.info("Created {}", it) }
    }

    @Transactional
    fun update(request: ClientRequest): Client {
        if (!clientRepository.existsById(request.id))
            throw RuntimeException("client id not found")
        return clientRepository.save(toClient(request))
                .also { log.info("Updated {}", it) }
    }

    @Transactional
    fun deleteById(id: Long) = clientRepository.deleteById(id)
            .also { log.info("Deleted Client(id={})", id) }

    private fun toClient(request: ClientRequest): Client {
        val type = clientTypeRepository.findById(request.typeId)
                .orElseThrow { IllegalArgumentException("client type id not found") }

        return Client(
                id = request.id,
                name = request.name,
                acronym = request.acronym,
                type = type)
    }

    companion object {
        private val log = loggerOf(ClientService::class)
    }

}
