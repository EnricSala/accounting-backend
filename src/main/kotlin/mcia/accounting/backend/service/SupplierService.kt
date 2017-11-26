package mcia.accounting.backend.service

import mcia.accounting.backend.entity.Supplier
import mcia.accounting.backend.repository.SupplierRepository
import mcia.accounting.backend.service.converter.SupplierConverter
import mcia.accounting.backend.service.request.SupplierRequest
import org.springframework.stereotype.Service

@Service
class SupplierService(repository: SupplierRepository, converter: SupplierConverter) :
        BaseService<Supplier, SupplierRequest>(Supplier.RESOURCE, repository, converter)
