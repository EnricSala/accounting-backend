package mcia.accounting.backend.controller

import mcia.accounting.backend.service.BaseService
import mcia.accounting.backend.utils.loggerOf
import org.slf4j.Logger
import org.springframework.web.bind.annotation.*

@RestController
abstract class BaseController<T, in R>(protected val path: String,
                                       protected val service: BaseService<T, R>) {

    protected val log: Logger by lazy { loggerOf(this::class) }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): T {
        log.debug("GET {}/{}", path, id)
        return service.findById(id)
    }

    @PostMapping
    fun create(@RequestBody request: R): T {
        log.debug("POST {} {}", path, request)
        return service.create(request)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: R): T {
        log.debug("PUT {}/{} {}", path, id, request)
        return service.update(id, request)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        log.debug("DELETE {}/{}", path, id)
        service.deleteById(id)
    }

}
