package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.ClientType
import mcia.accounting.backend.service.ClientTypeService
import mcia.accounting.backend.service.request.ClientTypeRequest
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ClientTypeController.PATH)
class ClientTypeController(service: ClientTypeService) :
        BaseController<ClientType, ClientTypeRequest>(PATH, service) {

    companion object {
        const val PATH = WebConfig.BASE_API_PATH + "/" + ClientType.RESOURCE
    }

}
