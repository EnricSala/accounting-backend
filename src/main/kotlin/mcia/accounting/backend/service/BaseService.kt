package mcia.accounting.backend.service

import mcia.accounting.backend.repository.SearchRepository
import mcia.accounting.backend.service.converter.RequestConverter
import mcia.accounting.backend.service.exception.ResourceNotFoundException
import mcia.accounting.backend.utils.loggerOf
import org.slf4j.Logger
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
abstract class BaseService<T, in R>(protected val resource: String,
                                    protected val repository: SearchRepository<T>,
                                    protected val converter: RequestConverter<T, R>) {

    protected val log: Logger by lazy { loggerOf(this::class) }

    @Transactional(readOnly = true)
    fun findAll(): Iterable<T> = repository.findAll()

    @Transactional(readOnly = true)
    fun findAll(sort: Sort): Iterable<T> = repository.findAll(sort)

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<T> = repository.findAll(pageable)

    @Transactional(readOnly = true)
    fun search(specification: Specification<T>): List<T> =
            repository.findAll(specification)

    @Transactional(readOnly = true)
    fun search(specification: Specification<T>, sort: Sort): List<T> =
            repository.findAll(specification, sort)

    @Transactional(readOnly = true)
    fun search(specification: Specification<T>, pageable: Pageable): Page<T> =
            repository.findAll(specification, pageable)

    @Transactional(readOnly = true)
    fun findById(id: Long): T = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("$resource id not found") }

    @Transactional
    fun create(request: R): T = repository.save(converter.toEntity(request))
            .also { log.info("Created {}", it) }

    @Transactional
    fun update(id: Long, request: R): T = when {
        repository.existsById(id) ->
            repository.save(converter.toEntity(request, id))
                    .also { log.info("Updated {}", it) }
        else -> throw ResourceNotFoundException("$resource id not found")
    }

    @Transactional
    fun deleteById(id: Long) = repository.deleteById(id)
            .also { log.info("Deleted $resource with id={}", id) }

}
