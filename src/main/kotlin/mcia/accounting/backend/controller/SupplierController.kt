package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.Supplier
import mcia.accounting.backend.service.SupplierService
import mcia.accounting.backend.service.request.SupplierRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(SupplierController.PATH)
class SupplierController(service: SupplierService) :
        BaseController<Supplier, SupplierRequest>(PATH, service) {

    @GetMapping
    fun findAll(): Iterable<Supplier> {
        log.debug("GET {}", PATH)
        return service.findAll()
    }

    companion object {
        const val PATH = WebConfig.BASE_API_PATH + "/" + Supplier.RESOURCE
    }

}
