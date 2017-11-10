package mcia.accounting.backend.service

import mcia.accounting.backend.entity.Purchase
import mcia.accounting.backend.repository.*
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
                      private val supplierRepository: SupplierRepository) {

    @Transactional(readOnly = true)
    fun search(specification: Specification<Purchase>,
               pageable: Pageable): Page<Purchase> =
            purchaseRepository.findAll(specification, pageable)

    @Transactional(readOnly = true)
    fun findById(id: Long): Purchase = purchaseRepository.findById(id)
            .orElseThrow { RuntimeException("purchase id not found") }

    @Transactional
    fun create(request: PurchaseRequest): Purchase = when {
        request.id < 0 ->
            purchaseRepository.save(toPurchase(request))
                    .also { log.info("Created {}", it) }
        else -> throw RuntimeException("id must not be set on create")
    }

    @Transactional
    fun update(request: PurchaseRequest): Purchase = when {
        purchaseRepository.existsById(request.id) ->
            purchaseRepository.save(toPurchase(request))
                    .also { log.info("Updated {}", it) }
        else -> throw RuntimeException("purchase id not found")
    }

    @Transactional
    fun deleteById(id: Long) = purchaseRepository.deleteById(id)
            .also { log.info("Deleted Purchase(id={})", id) }

    private fun toPurchase(request: PurchaseRequest): Purchase {
        val requestingEmployee = employeeRepository.findById(request.requestingEmployeeId)
                .orElseThrow { IllegalArgumentException("employee id not found") }
        val requestingProject = projectRepository.findById(request.requestingProjectId)
                .orElseThrow { IllegalArgumentException("project id not found") }
        val chargingProject = projectRepository.findById(request.chargingProjectId)
                .orElseThrow { IllegalArgumentException("project id not found") }
        val state = purchaseStateRepository.findById(request.stateId)
                .orElseThrow { IllegalArgumentException("purchase state id not found") }
        val type = purchaseTypeRepository.findById(request.typeId)
                .orElseThrow { IllegalArgumentException("purchase type id not found") }
        val supplier = supplierRepository.findById(request.supplierId)
                .orElseThrow { IllegalArgumentException("supplier id not found") }

        return Purchase(
                id = request.id,
                item = request.item,
                code = request.code,
                amount = request.amount,
                comments = request.comments,
                requestDate = request.requestDate,
                invoiceFile = request.invoiceFile,
                requestingEmployee = requestingEmployee,
                requestingProject = requestingProject,
                chargingProject = chargingProject,
                state = state,
                type = type,
                supplier = supplier)
    }

    companion object {
        private val log = loggerOf(PurchaseService::class)
    }

}
