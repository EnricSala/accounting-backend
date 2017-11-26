package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.Project
import mcia.accounting.backend.repository.search.SearchSpecification
import mcia.accounting.backend.service.ProjectService
import mcia.accounting.backend.service.request.ProjectRequest
import mcia.accounting.backend.utils.loggerOf
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(ProjectController.PATH)
class ProjectController(private val projectService: ProjectService) {

    @GetMapping
    fun search(@RequestParam(value = "q", defaultValue = "") query: String): Iterable<Project> {
        log.debug("GET {} q={}", PATH, query)
        return projectService.search(SearchSpecification.from(query))
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): Project {
        log.debug("GET {}/{}", PATH, id)
        return projectService.findById(id)
    }

    @PostMapping
    fun create(@RequestBody request: ProjectRequest): Project {
        log.debug("POST {} {}", PATH, request)
        return projectService.create(request)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: ProjectRequest): Project {
        log.debug("PUT {}/{} {}", PATH, id, request)
        return projectService.update(id, request)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        log.debug("DELETE {}/{}", PATH, id)
        return projectService.deleteById(id)
    }

    companion object {
        private val log = loggerOf(ProjectController::class)
        const val PATH = WebConfig.BASE_API_PATH + "/" + Project.RESOURCE
    }

}
