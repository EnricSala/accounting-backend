package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.PurchaseType
import mcia.accounting.backend.repository.PurchaseTypeRepository
import mcia.accounting.backend.utils.loggerOf
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(PurchaseTypeController.PATH)
class PurchaseTypeController(private val purchaseTypeRepository: PurchaseTypeRepository) {

    @GetMapping
    fun findAll(): Iterable<PurchaseType> {
        log.info("GET $PATH")
        return purchaseTypeRepository.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): PurchaseType {
        log.info("GET $PATH/$id")
        return purchaseTypeRepository.findById(id)
                .orElseThrow { RuntimeException("not found") }
    }

    @PostMapping
    fun create(@RequestBody purchaseType: PurchaseType): PurchaseType {
        log.info("POST $PATH $purchaseType")
        if (purchaseType.id < 0)
            return purchaseTypeRepository.save(purchaseType)
        else
            throw RuntimeException("insert cannot set the id")
    }

    @PutMapping
    fun update(@RequestBody purchaseType: PurchaseType): PurchaseType {
        log.info("PUT $PATH $purchaseType")
        if (purchaseTypeRepository.existsById(purchaseType.id))
            return purchaseTypeRepository.save(purchaseType)
        else
            throw RuntimeException("id not found")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        log.info("DELETE $PATH/$id")
        return purchaseTypeRepository.deleteById(id)
    }

    companion object {
        internal const val PATH = "${WebConfig.BASE_API_PATH}/purchase-type"
        private val log = loggerOf(PurchaseTypeController::class)
    }

}
