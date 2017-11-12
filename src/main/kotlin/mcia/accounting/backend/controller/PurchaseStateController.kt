package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.PurchaseState
import mcia.accounting.backend.repository.PurchaseStateRepository
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
                .orElseThrow { RuntimeException("not found") }
    }

    @PostMapping
    fun create(@RequestBody purchaseState: PurchaseState): PurchaseState {
        log.debug("POST {} {}", PATH, purchaseState)
        if (purchaseState.id < 0)
            return purchaseStateRepository.save(purchaseState)
        else
            throw RuntimeException("insert cannot set the id")
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody purchaseState: PurchaseState): PurchaseState {
        log.debug("PUT {}/{} {}", PATH, id, purchaseState)
        if (purchaseStateRepository.existsById(id)) {
            purchaseState.id = id
            return purchaseStateRepository.save(purchaseState)
        } else
            throw RuntimeException("id not found")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        log.debug("DELETE {}/{}", PATH, id)
        return purchaseStateRepository.deleteById(id)
    }

    companion object {
        internal const val PATH = "${WebConfig.BASE_API_PATH}/purchase-state"
        private val log = loggerOf(PurchaseStateController::class)
    }

}
