package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.PurchaseType
import mcia.accounting.backend.repository.PurchaseTypeRepository
import mcia.accounting.backend.service.exception.InvalidRequestException
import mcia.accounting.backend.service.exception.ResourceNotFoundException
import mcia.accounting.backend.utils.loggerOf
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(PurchaseTypeController.PATH)
class PurchaseTypeController(private val purchaseTypeRepository: PurchaseTypeRepository) {

    @GetMapping
    fun findAll(): Iterable<PurchaseType> {
        log.debug("GET {}", PATH)
        return purchaseTypeRepository.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): PurchaseType {
        log.debug("GET {}/{}", PATH, id)
        return purchaseTypeRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("purchase-type not found") }
    }

    @PostMapping
    fun create(@RequestBody purchaseType: PurchaseType): PurchaseType {
        log.debug("POST {} {}", PATH, purchaseType)
        if (purchaseType.id < 0)
            return purchaseTypeRepository.save(purchaseType)
        else
            throw InvalidRequestException("insert cannot set the id")
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody purchaseType: PurchaseType): PurchaseType {
        log.debug("PUT {}/{} {}", PATH, id, purchaseType)
        if (purchaseTypeRepository.existsById(id)) {
            purchaseType.id = id
            return purchaseTypeRepository.save(purchaseType)
        } else
            throw ResourceNotFoundException("purchase-type id not found")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        log.debug("DELETE {}/{}", PATH, id)
        return purchaseTypeRepository.deleteById(id)
    }

    companion object {
        private val log = loggerOf(PurchaseTypeController::class)
        const val PATH = WebConfig.BASE_API_PATH + "/" + PurchaseType.RESOURCE
    }

}
