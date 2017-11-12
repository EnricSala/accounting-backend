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
        log.debug("GET {}", PATH)
        return supplierRepository.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): Supplier {
        log.debug("GET {}/{}", PATH, id)
        return supplierRepository.findById(id)
                .orElseThrow { RuntimeException("not found") }
    }

    @PostMapping
    fun create(@RequestBody supplier: Supplier): Supplier {
        log.debug("POST {} {}", PATH, supplier)
        if (supplier.id < 0)
            return supplierRepository.save(supplier)
        else
            throw RuntimeException("insert cannot set the id")
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody supplier: Supplier): Supplier {
        log.debug("PUT {}/{} {}", PATH, id, supplier)
        if (supplierRepository.existsById(id)) {
            supplier.id = id
            return supplierRepository.save(supplier)
        } else
            throw RuntimeException("id not found")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        log.debug("DELETE {}/{}", PATH, id)
        return supplierRepository.deleteById(id)
    }

    companion object {
        internal const val PATH = "${WebConfig.BASE_API_PATH}/supplier"
        private val log = loggerOf(SupplierController::class)
    }

}
