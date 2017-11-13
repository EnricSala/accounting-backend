package mcia.accounting.backend.service

import mcia.accounting.backend.entity.Purchase
import mcia.accounting.backend.repository.*
import mcia.accounting.backend.service.exception.ResourceNotFoundException
import mcia.accounting.backend.service.request.PurchaseRequest
import mcia.accounting.backend.utils.loggerOf
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PurchaseService(private val purchaseRepository: PurchaseRepository,
                      private val purchaseStateRepository: PurchaseStateRepository,
                      private val purchaseTypeRepository: PurchaseTypeRepository,
                      private val projectRepository: ProjectRepository,
                      private val employeeRepository: EmployeeRepository,
                      private val supplierRepository: SupplierRepository,
                      private val invoiceService: InvoiceService) {

    @Transactional(readOnly = true)
    fun search(specification: Specification<Purchase>,
               pageable: Pageable): Page<Purchase> =
            purchaseRepository.findAll(specification, pageable)

    @Transactional(readOnly = true)
    fun findById(id: Long): Purchase = purchaseRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("purchase id not found") }

    @Transactional
    fun create(request: PurchaseRequest): Purchase =
            purchaseRepository.save(toPurchase(request))
                    .also { log.info("Created {}", it) }

    @Transactional
    fun update(id: Long, request: PurchaseRequest): Purchase = when {
        purchaseRepository.existsById(id) ->
            purchaseRepository.save(toPurchase(request, id))
                    .also { log.info("Updated {}", it) }
        else -> throw ResourceNotFoundException("purchase id not found")
    }

    @Transactional
    fun deleteById(id: Long) {
        invoiceService.deleteInvoiceOf(id)
        purchaseRepository.deleteById(id)
        log.info("Deleted Purchase(id={})", id)
    }

    private fun toPurchase(request: PurchaseRequest, purchaseId: Long = -1): Purchase {
        val requestingEmployee = employeeRepository.findById(request.requestingEmployeeId)
                .orElseThrow { ResourceNotFoundException("employee id not found") }
        val requestingProject = projectRepository.findById(request.requestingProjectId)
                .orElseThrow { ResourceNotFoundException("project id not found") }
        val chargingProject = projectRepository.findById(request.chargingProjectId)
                .orElseThrow { ResourceNotFoundException("project id not found") }
        val state = purchaseStateRepository.findById(request.stateId)
                .orElseThrow { ResourceNotFoundException("purchase-state id not found") }
        val type = purchaseTypeRepository.findById(request.typeId)
                .orElseThrow { ResourceNotFoundException("purchase-type id not found") }
        val supplier = supplierRepository.findById(request.supplierId)
                .orElseThrow { ResourceNotFoundException("supplier id not found") }
        return Purchase(
                id = purchaseId,
                item = request.item,
                code = request.code,
                amount = request.amount,
                comments = request.comments,
                requestDate = request.requestDate,
                invoicePath = invoicePathOf(purchaseId),
                requestingEmployee = requestingEmployee,
                requestingProject = requestingProject,
                chargingProject = chargingProject,
                state = state,
                type = type,
                supplier = supplier)
    }

    private fun invoicePathOf(purchaseId: Long): String? = when {
        purchaseId < 0 -> null
        else -> purchaseRepository.findById(purchaseId)
                .map { it.invoicePath }.orElse(null)
    }

    companion object {
        private val log = loggerOf(PurchaseService::class)
    }

}
