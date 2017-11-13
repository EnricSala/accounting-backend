package mcia.accounting.backend.service

import mcia.accounting.backend.entity.Project
import mcia.accounting.backend.repository.ClientRepository
import mcia.accounting.backend.repository.EmployeeRepository
import mcia.accounting.backend.repository.ProjectRepository
import mcia.accounting.backend.repository.ProjectTypeRepository
import mcia.accounting.backend.service.exception.ResourceNotFoundException
import mcia.accounting.backend.service.request.ProjectRequest
import mcia.accounting.backend.utils.loggerOf
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProjectService(private val projectRepository: ProjectRepository,
                     private val projectTypeRepository: ProjectTypeRepository,
                     private val clientRepository: ClientRepository,
                     private val employeeRepository: EmployeeRepository) {

    @Transactional(readOnly = true)
    fun search(specification: Specification<Project>): Iterable<Project> =
            projectRepository.findAll(specification)

    @Transactional(readOnly = true)
    fun findById(id: Long): Project = projectRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("project id not found") }

    @Transactional
    fun create(request: ProjectRequest): Project =
            projectRepository.save(toProject(request))
                    .also { log.info("Created {}", it) }

    @Transactional
    fun update(id: Long, request: ProjectRequest): Project = when {
        projectRepository.existsById(id) ->
            projectRepository.save(toProject(request, id))
                    .also { log.info("Updated {}", it) }
        else -> throw ResourceNotFoundException("project id not found")
    }

    @Transactional
    fun deleteById(id: Long) = projectRepository.deleteById(id)
            .also { log.info("Deleted Project(id={})", id) }

    private fun toProject(request: ProjectRequest, id: Long = -1): Project {
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

    companion object {
        private val log = loggerOf(ProjectService::class)
    }

}
