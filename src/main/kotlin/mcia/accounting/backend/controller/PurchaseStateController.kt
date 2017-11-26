package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.PurchaseState
import mcia.accounting.backend.repository.PurchaseStateRepository
import mcia.accounting.backend.service.exception.InvalidRequestException
import mcia.accounting.backend.service.exception.ResourceNotFoundException
import mcia.accounting.backend.utils.loggerOf
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(PurchaseStateController.PATH)
class PurchaseStateController(private val purchaseStateRepository: PurchaseStateRepository) {

    @GetMapping
    fun findAll(): Iterable<PurchaseState> {
        log.debug("GET {}", PATH)
        return purchaseStateRepository.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): PurchaseState {
        log.debug("GET {}/{}", PATH, id)
        return purchaseStateRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("purchase-state not found") }
    }

    @PostMapping
    fun create(@RequestBody purchaseState: PurchaseState): PurchaseState {
        log.debug("POST {} {}", PATH, purchaseState)
        if (purchaseState.id < 0)
            return purchaseStateRepository.save(purchaseState)
        else
            throw InvalidRequestException("insert cannot set the id")
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody purchaseState: PurchaseState): PurchaseState {
        log.debug("PUT {}/{} {}", PATH, id, purchaseState)
        if (purchaseStateRepository.existsById(id)) {
            purchaseState.id = id
            return purchaseStateRepository.save(purchaseState)
        } else
            throw ResourceNotFoundException("purchase-state id not found")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        log.debug("DELETE {}/{}", PATH, id)
        return purchaseStateRepository.deleteById(id)
    }

    companion object {
        private val log = loggerOf(PurchaseStateController::class)
        const val PATH = WebConfig.BASE_API_PATH + "/" + PurchaseState.RESOURCE
    }

}
