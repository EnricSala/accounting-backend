package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.ProjectType
import mcia.accounting.backend.service.ProjectTypeService
import mcia.accounting.backend.service.request.ProjectTypeRequest
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ProjectTypeController.PATH)
class ProjectTypeController(service: ProjectTypeService) :
        BaseController<ProjectType, ProjectTypeRequest>(PATH, service) {

    companion object {
        const val PATH = WebConfig.BASE_API_PATH + "/" + ProjectType.RESOURCE
    }

}
