package mcia.accounting.backend.service.converter

import mcia.accounting.backend.entity.ProjectType
import mcia.accounting.backend.service.request.ProjectTypeRequest
import org.springframework.stereotype.Component

@Component
class ProjectTypeConverter : RequestConverter<ProjectType, ProjectTypeRequest> {

    override fun toEntity(request: ProjectTypeRequest, id: Long): ProjectType =
            ProjectType(
                    id = id,
                    name = request.name)

}
