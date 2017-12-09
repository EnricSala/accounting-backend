package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.PurchaseType
import mcia.accounting.backend.service.PurchaseTypeService
import mcia.accounting.backend.service.request.PurchaseTypeRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PurchaseTypeController.PATH)
class PurchaseTypeController(service: PurchaseTypeService) :
        BaseController<PurchaseType, PurchaseTypeRequest>(PATH, service) {

    @GetMapping
    fun findAll(): Iterable<PurchaseType> {
        log.debug("GET {}", PATH)
        return service.findAll()
    }

    companion object {
        const val PATH = WebConfig.BASE_API_PATH + "/" + PurchaseType.RESOURCE
    }

}
