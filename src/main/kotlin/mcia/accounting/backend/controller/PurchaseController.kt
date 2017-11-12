package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.controller.dto.PageResult
import mcia.accounting.backend.entity.Purchase
import mcia.accounting.backend.repository.search.SearchSpecification
import mcia.accounting.backend.service.PurchaseService
import mcia.accounting.backend.service.request.PurchaseRequest
import mcia.accounting.backend.utils.loggerOf
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Order
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(PurchaseController.PATH)
class PurchaseController(private val purchaseService: PurchaseService) {

    @GetMapping
    fun search(@RequestParam(value = "q", defaultValue = "") query: String,
               @RequestParam(value = "page", defaultValue = "0") page: Int,
               @RequestParam(value = "size", defaultValue = "10") size: Int): PageResult<Purchase> {
        log.debug("GET {} q={} page={} size={}", PATH, query, page, size)
        val sort = Sort.by(Order.desc("requestDate"))
        val pageable = PageRequest.of(page, size, sort)
        val specification = SearchSpecification.from<Purchase>(query)
        return PageResult.of(purchaseService.search(specification, pageable))
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): Purchase {
        log.debug("GET {}/{}", PATH, id)
        return purchaseService.findById(id)
    }

    @PostMapping
    fun create(@RequestBody request: PurchaseRequest): Purchase {
        log.debug("POST {} {}", PATH, request)
        return purchaseService.create(request)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: PurchaseRequest): Purchase {
        log.debug("PUT {}/{} {}", PATH, id, request)
        return purchaseService.update(id, request)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        log.debug("DELETE {}/{}", PATH, id)
        return purchaseService.deleteById(id)
    }

    companion object {
        internal const val PATH = "${WebConfig.BASE_API_PATH}/purchase"
        private val log = loggerOf(PurchaseController::class)
    }

}
