package mcia.accounting.backend.service

import mcia.accounting.backend.entity.ProjectType
import mcia.accounting.backend.repository.ProjectTypeRepository
import mcia.accounting.backend.service.converter.ProjectTypeConverter
import mcia.accounting.backend.service.request.ProjectTypeRequest
import org.springframework.stereotype.Service

@Service
class ProjectTypeService(repository: ProjectTypeRepository, converter: ProjectTypeConverter) :
        BaseService<ProjectType, ProjectTypeRequest>(ProjectType.RESOURCE, repository, converter)
