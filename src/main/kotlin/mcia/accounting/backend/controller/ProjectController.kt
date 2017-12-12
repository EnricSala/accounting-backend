package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.Project
import mcia.accounting.backend.repository.search.SearchSpecification
import mcia.accounting.backend.service.ProjectService
import mcia.accounting.backend.service.request.ProjectRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Order
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ProjectController.PATH)
class ProjectController(service: ProjectService) :
        BaseController<Project, ProjectRequest>(PATH, service) {

    override fun findBy(query: String, pageable: Pageable): Iterable<Project> {
        val sort = pageable.getSortOr(DEFAULT_SORT)
        log.debug("GET {} q={} sort={}", PATH, query, sort)
        val specification = SearchSpecification.from<Project>(query)
        return service.search(specification, sort)
    }

    companion object {
        const val PATH = WebConfig.BASE_API_PATH + "/" + Project.RESOURCE
        private val DEFAULT_SORT = Sort.by(Order.asc("name"))
    }

}
