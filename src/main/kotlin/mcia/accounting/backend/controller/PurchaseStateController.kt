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
        log.info("GET $PATH")
        return purchaseStateRepository.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): PurchaseState {
        log.info("GET $PATH/$id")
        return purchaseStateRepository.findById(id)
                .orElseThrow { RuntimeException("not found") }
    }

    @PostMapping
    fun create(@RequestBody purchaseState: PurchaseState): PurchaseState {
        log.info("POST $PATH $purchaseState")
        if (purchaseState.id < 0)
            throw RuntimeException("insert cannot set the id")
        return purchaseStateRepository.save(purchaseState)
    }

    @PutMapping("/{id}")
    fun update(@RequestBody purchaseState: PurchaseState): PurchaseState {
        log.info("PUT $PATH $purchaseState")
        if (!purchaseStateRepository.existsById(purchaseState.id))
            throw RuntimeException("id not found")
        return purchaseStateRepository.save(purchaseState)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        log.info("DELETE $PATH/$id")
        return purchaseStateRepository.deleteById(id)
    }

    companion object {
        internal const val PATH = "${WebConfig.BASE_API_PATH}/purchase-state"
        private val log = loggerOf(PurchaseStateController::class)
    }

}
