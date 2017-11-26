package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.Client
import mcia.accounting.backend.service.ClientService
import mcia.accounting.backend.service.request.ClientRequest
import mcia.accounting.backend.utils.loggerOf
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(ClientController.PATH)
class ClientController(private val clientService: ClientService) {

    @GetMapping
    fun findAll(): Iterable<Client> {
        log.debug("GET {}", PATH)
        return clientService.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): Client {
        log.debug("GET {}/{}", PATH, id)
        return clientService.findById(id)
    }

    @PostMapping
    fun create(@RequestBody request: ClientRequest): Client {
        log.debug("POST {} {}", PATH, request)
        return clientService.create(request)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: ClientRequest): Client {
        log.debug("PUT {}/{} {}", PATH, id, request)
        return clientService.update(id, request)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        log.debug("DELETE {}/{}", PATH, id)
        return clientService.deleteById(id)
    }

    companion object {
        private val log = loggerOf(ClientController::class)
        const val PATH = WebConfig.BASE_API_PATH + "/" + Client.RESOURCE
    }

}
