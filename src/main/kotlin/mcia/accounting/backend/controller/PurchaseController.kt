package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.controller.dto.PageResult
import mcia.accounting.backend.entity.Purchase
import mcia.accounting.backend.repository.search.SearchSpecification
import mcia.accounting.backend.service.InvoiceService
import mcia.accounting.backend.service.PurchaseService
import mcia.accounting.backend.service.request.PurchaseRequest
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
class PurchaseController(service: PurchaseService, private val invoiceService: InvoiceService) :
        BaseController<Purchase, PurchaseRequest>(PATH, service) {

    @GetMapping
    fun search(@RequestParam(value = "q", defaultValue = "") query: String,
               @RequestParam(value = "page", defaultValue = "0") page: Int,
               @RequestParam(value = "size", defaultValue = "10") size: Int): PageResult<Purchase> {
        log.debug("GET {} q={} page={} size={}", PATH, query, page, size)
        val sort = Sort.by(Order.desc("requestDate"))
        val pageable = PageRequest.of(page, size, sort)
        val specification = SearchSpecification.from<Purchase>(query)
        return PageResult.of(service.search(specification, pageable))
    }

    @GetMapping("/{id}/invoice", produces = [INVOICE_MIME])
    fun findInvoice(@PathVariable id: Long, response: HttpServletResponse): Resource {
        log.debug("GET {}/{}/invoice", PATH, id)
        return invoiceService.findInvoiceOf(id).also {
            log.debug("Downloading invoice: ${it.filename}")
            response.setHeader(DISPOSITION_HEADER, DISPOSITION_VALUE + it.filename)
        }
    }

    @PutMapping("/{id}/invoice")
    fun updateInvoice(@PathVariable id: Long, @RequestPart file: MultipartFile) {
        log.debug("PUT {}/{}/invoice", PATH, id)
        invoiceService.saveInvoiceTo(id, file)
    }

    @DeleteMapping("/{id}/invoice")
    fun deleteInvoice(@PathVariable id: Long) {
        log.debug("DELETE {}/{}/invoice", PATH, id)
        invoiceService.deleteInvoiceOf(id)
    }

    companion object {
        const val PATH = WebConfig.BASE_API_PATH + "/" + Purchase.RESOURCE
        private const val INVOICE_MIME = MediaType.APPLICATION_PDF_VALUE
        private const val DISPOSITION_HEADER = "Content-Disposition"
        private const val DISPOSITION_VALUE = "inline; filename="
    }

}
