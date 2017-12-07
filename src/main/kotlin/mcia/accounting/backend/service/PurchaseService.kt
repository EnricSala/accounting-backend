package mcia.accounting.backend.service

import mcia.accounting.backend.entity.Purchase
import mcia.accounting.backend.repository.PurchaseRepository
import mcia.accounting.backend.service.converter.PurchaseConverter
import mcia.accounting.backend.service.request.PurchaseRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PurchaseService(repository: PurchaseRepository,
                      converter: PurchaseConverter,
                      private val invoiceService: InvoiceService) :
        BaseService<Purchase, PurchaseRequest>(Purchase.RESOURCE, repository, converter) {

    @Transactional
    override fun deleteById(id: Long) {
        invoiceService.deleteInvoiceOf(id)
        super.deleteById(id)
    }

}
