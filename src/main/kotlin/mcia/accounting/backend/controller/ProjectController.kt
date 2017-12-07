package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.controller.dto.PageResult
import mcia.accounting.backend.entity.Project
import mcia.accounting.backend.repository.search.SearchSpecification
import mcia.accounting.backend.service.ProjectService
import mcia.accounting.backend.service.request.ProjectRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ProjectController.PATH)
class ProjectController(service: ProjectService) :
        BaseController<Project, ProjectRequest>(PATH, service) {

    override fun findBy(query: String, page: Int, size: Int): PageResult<Project> {
        val sort = Sort.by(Sort.Order.asc("name"))
        val specification = SearchSpecification.from<Project>(query)
        return PageResult.just(service.search(specification, sort))
    }

    companion object {
        const val PATH = WebConfig.BASE_API_PATH + "/" + Project.RESOURCE
    }

}
