package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.ClientType
import mcia.accounting.backend.repository.ClientTypeRepository
import mcia.accounting.backend.service.exception.InvalidRequestException
import mcia.accounting.backend.service.exception.ResourceNotFoundException
import mcia.accounting.backend.utils.loggerOf
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(ClientTypeController.PATH)
class ClientTypeController(private val clientTypeRepository: ClientTypeRepository) {

    @GetMapping
    fun findAll(): Iterable<ClientType> {
        log.debug("GET {}", PATH)
        return clientTypeRepository.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ClientType {
        log.debug("GET {}/{}", PATH, id)
        return clientTypeRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("client-type id not found") }
    }

    @PostMapping
    fun create(@RequestBody clientType: ClientType): ClientType {
        log.debug("POST {} {}", PATH, clientType)
        if (clientType.id < 0)
            return clientTypeRepository.save(clientType)
        else
            throw InvalidRequestException("insert cannot set the id")
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody clientType: ClientType): ClientType {
        log.debug("PUT {}/{} {}", PATH, id, clientType)
        if (clientTypeRepository.existsById(id)) {
            clientType.id = id
            return clientTypeRepository.save(clientType)
        } else
            throw ResourceNotFoundException("client-type id not found")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        log.debug("DELETE {}/{}", PATH, id)
        return clientTypeRepository.deleteById(id)
    }

    companion object {
        private val log = loggerOf(ClientTypeController::class)
        const val PATH = WebConfig.BASE_API_PATH + "/" + ClientType.RESOURCE
    }

}
