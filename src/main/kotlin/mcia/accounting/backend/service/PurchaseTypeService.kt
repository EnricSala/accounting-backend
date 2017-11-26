package mcia.accounting.backend.service

import mcia.accounting.backend.entity.PurchaseType
import mcia.accounting.backend.repository.PurchaseTypeRepository
import mcia.accounting.backend.service.converter.PurchaseTypeConverter
import mcia.accounting.backend.service.request.PurchaseTypeRequest
import org.springframework.stereotype.Service

@Service
class PurchaseTypeService(repository: PurchaseTypeRepository, converter: PurchaseTypeConverter) :
        BaseService<PurchaseType, PurchaseTypeRequest>(PurchaseType.RESOURCE, repository, converter)
