package mcia.accounting.backend.service.converter

import mcia.accounting.backend.entity.Project
import mcia.accounting.backend.repository.ClientRepository
import mcia.accounting.backend.repository.EmployeeRepository
import mcia.accounting.backend.repository.ProjectTypeRepository
import mcia.accounting.backend.service.exception.ResourceNotFoundException
import mcia.accounting.backend.service.request.ProjectRequest
import org.springframework.stereotype.Component

@Component
class ProjectConverter(private val projectTypeRepository: ProjectTypeRepository,
                       private val employeeRepository: EmployeeRepository,
                       private val clientRepository: ClientRepository) : RequestConverter<Project, ProjectRequest> {

    override fun toEntity(request: ProjectRequest, id: Long): Project {
        val type = projectTypeRepository.findById(request.typeId)
                .orElseThrow { ResourceNotFoundException("project-type id not found") }
        val client = clientRepository.findById(request.clientId)
                .orElseThrow { ResourceNotFoundException("client id not found") }
        val manager = employeeRepository.findById(request.managerId)
                .orElseThrow { ResourceNotFoundException("manager id not found") }
        return Project(
                id = id,
                name = request.name,
                description = request.description,
                code = request.code,
                finished = request.finished,
                manager = manager,
                client = client,
                type = type)
    }

}
