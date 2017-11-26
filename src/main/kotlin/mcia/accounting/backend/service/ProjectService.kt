package mcia.accounting.backend.service

import mcia.accounting.backend.entity.Project
import mcia.accounting.backend.repository.ProjectRepository
import mcia.accounting.backend.service.converter.ProjectConverter
import mcia.accounting.backend.service.request.ProjectRequest
import org.springframework.stereotype.Service

@Service
class ProjectService(repository: ProjectRepository, converter: ProjectConverter) :
        BaseService<Project, ProjectRequest>(Project.RESOURCE, repository, converter)
