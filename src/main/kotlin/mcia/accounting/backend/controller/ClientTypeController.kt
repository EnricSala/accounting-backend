package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.ClientType
import mcia.accounting.backend.repository.ClientTypeRepository
import mcia.accounting.backend.utils.loggerOf
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(ClientTypeController.PATH)
class ClientTypeController(private val clientTypeRepository: ClientTypeRepository) {

    @GetMapping
    fun findAll(): Iterable<ClientType> {
        log.info("GET $PATH")
        return clientTypeRepository.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ClientType {
        log.info("GET $PATH/$id")
        return clientTypeRepository.findById(id)
                .orElseThrow { RuntimeException("not found") }
    }

    @PostMapping
    fun create(@RequestBody clientType: ClientType): ClientType {
        log.info("POST $PATH $clientType")
        if (clientType.id < 0)
            return clientTypeRepository.save(clientType)
        else
            throw RuntimeException("insert cannot set the id")
    }

    @PutMapping
    fun update(@RequestBody clientType: ClientType): ClientType {
        log.info("PUT $PATH $clientType")
        if (clientTypeRepository.existsById(clientType.id))
            return clientTypeRepository.save(clientType)
        else
            throw RuntimeException("id not found")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        log.info("DELETE $PATH/$id")
        return clientTypeRepository.deleteById(id)
    }

    companion object {
        internal const val PATH = "${WebConfig.BASE_API_PATH}/client-type"
        private val log = loggerOf(ClientTypeController::class)
    }

}
