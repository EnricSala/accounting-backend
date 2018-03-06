package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.controller.dto.PageResult
import mcia.accounting.backend.entity.Purchase
import mcia.accounting.backend.repository.search.SearchSpecification
import mcia.accounting.backend.service.InvoiceService
import mcia.accounting.backend.service.PurchaseService
import mcia.accounting.backend.service.request.PurchaseRequest
import mcia.accounting.backend.utils.withDefaultSort
import org.springframework.core.io.Resource
import org.springframework.data.domain.Pageable
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

    override fun findBy(query: String, pageable: Pageable): Iterable<Purchase> {
        val pageBy = pageable.withDefaultSort(DEFAULT_SORT)
        log.debug("GET {} q={} page={} size={} sort={}",
                PATH, query, pageBy.pageNumber, pageBy.pageSize, pageBy.sort)
        val specification = SearchSpecification.from<Purchase>(query)
        return PageResult.of(service.search(specification, pageBy))
    }

    @GetMapping("/{id}/invoice", produces = [INVOICE_MIME])
    fun findInvoice(@PathVariable id: Long, response: HttpServletResponse): Resource {
        log.debug("GET {}/{}/invoice", PATH, id)
        val (resource, filename) = invoiceService.findInvoiceOf(id)
        log.debug("Downloading invoice: ${resource.filename} as $filename")
        response.setHeader(DISPOSITION_HEADER, DISPOSITION_VALUE + filename)
        return resource
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
        private val DEFAULT_SORT = Sort.by(Order.desc("requestDate"))
        private const val INVOICE_MIME = MediaType.APPLICATION_PDF_VALUE
        private const val DISPOSITION_HEADER = "Content-Disposition"
        private const val DISPOSITION_VALUE = "inline; filename="
    }

}
