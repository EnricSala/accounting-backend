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
    fun create(request: ClientRequest): Client =
            clientRepository.save(toClient(request))
                    .also { log.info("Created {}", it) }

    @Transactional
    fun update(id: Long, request: ClientRequest): Client = when {
        clientRepository.existsById(id) ->
            clientRepository.save(toClient(request, id))
                    .also { log.info("Updated {}", it) }
        else -> throw RuntimeException("client id not found")
    }

    @Transactional
    fun deleteById(id: Long) = clientRepository.deleteById(id)
            .also { log.info("Deleted Client(id={})", id) }

    private fun toClient(request: ClientRequest, id: Long = -1): Client {
        val type = clientTypeRepository.findById(request.typeId)
                .orElseThrow { IllegalArgumentException("client type id not found") }
        return Client(
                id = id,
                name = request.name,
                acronym = request.acronym,
                type = type)
    }

    companion object {
        private val log = loggerOf(ClientService::class)
    }

}
