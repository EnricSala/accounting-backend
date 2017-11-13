package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.controller.dto.PageResult
import mcia.accounting.backend.entity.Purchase
import mcia.accounting.backend.repository.search.SearchSpecification
import mcia.accounting.backend.service.InvoiceService
import mcia.accounting.backend.service.PurchaseService
import mcia.accounting.backend.service.request.PurchaseRequest
import mcia.accounting.backend.utils.loggerOf
import org.springframework.core.io.Resource
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Order
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping(PurchaseController.PATH)
class PurchaseController(private val purchaseService: PurchaseService,
                         private val invoiceService: InvoiceService) {

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

    @GetMapping("/{id}/invoice", produces = arrayOf(INVOICE_MIME))
    fun findInvoice(@PathVariable id: Long, response: HttpServletResponse): Resource {
        log.debug("GET {}/{}/invoice", PATH, id)
        return invoiceService.findInvoiceOf(id).also {
            log.debug("Downloading invoice: ${it.filename}")
            response.setHeader(DISPOSITION_HEADER, DISPOSITION_VALUE + it.filename)
        }
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

    @PutMapping("/{id}/invoice")
    fun updateInvoice(@PathVariable id: Long, @RequestPart file: MultipartFile) {
        log.debug("PUT {}/{}/invoice", PATH, id)
        invoiceService.saveInvoiceTo(id, file)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        log.debug("DELETE {}/{}", PATH, id)
        return purchaseService.deleteById(id)
    }

    @DeleteMapping("/{id}/invoice")
    fun deleteInvoice(@PathVariable id: Long) {
        log.debug("DELETE {}/{}/invoice", PATH, id)
        invoiceService.deleteInvoiceOf(id)
    }

    companion object {
        private val log = loggerOf(PurchaseController::class)
        internal const val PATH = "${WebConfig.BASE_API_PATH}/purchase"
        private const val INVOICE_MIME = MediaType.APPLICATION_PDF_VALUE
        private const val DISPOSITION_HEADER = "Content-Disposition"
        private const val DISPOSITION_VALUE = "inline; filename="
    }

}
