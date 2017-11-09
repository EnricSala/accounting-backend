package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.Supplier
import mcia.accounting.backend.repository.SupplierRepository
import mcia.accounting.backend.utils.loggerOf
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(SupplierController.PATH)
class SupplierController(private val supplierRepository: SupplierRepository) {

    @GetMapping
    fun findAll(): Iterable<Supplier> {
        log.info("GET $PATH")
        return supplierRepository.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): Supplier {
        log.info("GET $PATH/$id")
        return supplierRepository.findById(id)
                .orElseThrow { RuntimeException("not found") }
    }

    @PostMapping
    fun create(@RequestBody supplier: Supplier): Supplier {
        log.info("POST $PATH $supplier")
        if (supplier.id < 0)
            throw RuntimeException("insert cannot set the id")
        return supplierRepository.save(supplier)
    }

    @PutMapping("/{id}")
    fun update(@RequestBody supplier: Supplier): Supplier {
        log.info("PUT $PATH $supplier")
        if (!supplierRepository.existsById(supplier.id))
            throw RuntimeException("id not found")
        return supplierRepository.save(supplier)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        log.info("DELETE $PATH/$id")
        return supplierRepository.deleteById(id)
    }

    companion object {
        internal const val PATH = "${WebConfig.BASE_API_PATH}/supplier"
        private val log = loggerOf(SupplierController::class)
    }

}
