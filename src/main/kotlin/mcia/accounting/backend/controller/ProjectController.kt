package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.Project
import mcia.accounting.backend.repository.search.SearchSpecification
import mcia.accounting.backend.service.ProjectService
import mcia.accounting.backend.service.request.ProjectRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Order
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ProjectController.PATH)
class ProjectController(service: ProjectService) :
        BaseController<Project, ProjectRequest>(PATH, service) {

    @GetMapping
    fun search(@RequestParam(value = "q", defaultValue = "") query: String): List<Project> {
        log.debug("GET {} q={}", PATH, query)
        val sort = Sort.by(Order.asc("name"))
        val specification = SearchSpecification.from<Project>(query)
        return service.search(specification, sort)
    }

    companion object {
        const val PATH = WebConfig.BASE_API_PATH + "/" + Project.RESOURCE
    }

}
