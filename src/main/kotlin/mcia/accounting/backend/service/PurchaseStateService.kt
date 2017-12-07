package mcia.accounting.backend.service

import mcia.accounting.backend.entity.PurchaseState
import mcia.accounting.backend.repository.PurchaseStateRepository
import mcia.accounting.backend.service.converter.PurchaseStateConverter
import mcia.accounting.backend.service.request.PurchaseStateRequest
import org.springframework.stereotype.Service

@Service
class PurchaseStateService(repository: PurchaseStateRepository, converter: PurchaseStateConverter) :
        BaseService<PurchaseState, PurchaseStateRequest>(PurchaseState.RESOURCE, repository, converter)
