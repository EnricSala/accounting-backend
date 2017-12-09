package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.Client
import mcia.accounting.backend.service.ClientService
import mcia.accounting.backend.service.request.ClientRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ClientController.PATH)
class ClientController(service: ClientService) :
        BaseController<Client, ClientRequest>(PATH, service) {

    @GetMapping
    fun findAll(): Iterable<Client> {
        log.debug("GET {}", PATH)
        return service.findAll()
    }

    companion object {
        const val PATH = WebConfig.BASE_API_PATH + "/" + Client.RESOURCE
    }

}
