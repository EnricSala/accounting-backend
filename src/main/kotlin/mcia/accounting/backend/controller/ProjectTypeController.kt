package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.ProjectType
import mcia.accounting.backend.repository.ProjectTypeRepository
import mcia.accounting.backend.service.exception.InvalidRequestException
import mcia.accounting.backend.service.exception.ResourceNotFoundException
import mcia.accounting.backend.utils.loggerOf
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(ProjectTypeController.PATH)
class ProjectTypeController(private val projectTypeRepository: ProjectTypeRepository) {

    @GetMapping
    fun findAll(): Iterable<ProjectType> {
        log.debug("GET {}", PATH)
        return projectTypeRepository.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ProjectType {
        log.debug("GET {}/{}", PATH, id)
        return projectTypeRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("project-type not found") }
    }

    @PostMapping
    fun create(@RequestBody projectType: ProjectType): ProjectType {
        log.debug("POST {} {}", PATH, projectType)
        if (projectType.id < 0)
            return projectTypeRepository.save(projectType)
        else
            throw InvalidRequestException("insert cannot set the id")
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody projectType: ProjectType): ProjectType {
        log.debug("PUT {}/{} {}", PATH, id, projectType)
        if (projectTypeRepository.existsById(id)) {
            projectType.id = id
            return projectTypeRepository.save(projectType)
        } else
            throw ResourceNotFoundException("project-type id not found")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        log.debug("DELETE {}/{}", PATH, id)
        return projectTypeRepository.deleteById(id)
    }

    companion object {
        private val log = loggerOf(ProjectTypeController::class)
        const val PATH = WebConfig.BASE_API_PATH + "/" + ProjectType.RESOURCE
    }

}
