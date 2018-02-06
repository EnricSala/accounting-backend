package mcia.accounting.backend.service.converter

import mcia.accounting.backend.entity.Purchase
import mcia.accounting.backend.repository.*
import mcia.accounting.backend.service.exception.ResourceNotFoundException
import mcia.accounting.backend.service.request.PurchaseRequest
import org.springframework.stereotype.Component

@Component
class PurchaseConverter(private val purchaseRepository: PurchaseRepository,
                        private val purchaseStateRepository: PurchaseStateRepository,
                        private val purchaseTypeRepository: PurchaseTypeRepository,
                        private val projectRepository: ProjectRepository,
                        private val employeeRepository: EmployeeRepository,
                        private val supplierRepository: SupplierRepository) : RequestConverter<Purchase, PurchaseRequest> {

    override fun toEntity(request: PurchaseRequest, id: Long): Purchase {
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
                id = id,
                item = request.item,
                code = request.code,
                codeRP = request.codeRP,
                codeLV = request.codeLV,
                amount = request.amount,
                comments = request.comments,
                requestDate = request.requestDate,
                invoicePath = invoicePathOf(id),
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

}
