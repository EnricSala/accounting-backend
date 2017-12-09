package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.PurchaseState
import mcia.accounting.backend.service.PurchaseStateService
import mcia.accounting.backend.service.request.PurchaseStateRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PurchaseStateController.PATH)
class PurchaseStateController(service: PurchaseStateService) :
        BaseController<PurchaseState, PurchaseStateRequest>(PATH, service) {

    @GetMapping
    fun findAll(): Iterable<PurchaseState> {
        log.debug("GET {}", PATH)
        return service.findAll()
    }

    companion object {
        const val PATH = WebConfig.BASE_API_PATH + "/" + PurchaseState.RESOURCE
    }

}
