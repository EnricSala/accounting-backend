package mcia.accounting.backend.service

import mcia.accounting.backend.entity.Project
import mcia.accounting.backend.repository.ClientRepository
import mcia.accounting.backend.repository.EmployeeRepository
import mcia.accounting.backend.repository.ProjectRepository
import mcia.accounting.backend.repository.ProjectTypeRepository
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
            .orElseThrow { RuntimeException("project id not found") }

    @Transactional
    fun create(request: ProjectRequest): Project {
        if (request.id < 0)
            throw RuntimeException("id must not be set on create")
        return projectRepository.save(toProject(request))
                .also { log.info("Created {}", it) }
    }

    @Transactional
    fun update(request: ProjectRequest): Project {
        if (!projectRepository.existsById(request.id))
            throw RuntimeException("project id not found")
        return projectRepository.save(toProject(request))
                .also { log.info("Updated {}", it) }
    }

    @Transactional
    fun deleteById(id: Long) = projectRepository.deleteById(id)
            .also { log.info("Deleted Project(id={})", id) }

    private fun toProject(request: ProjectRequest): Project {
        val type = projectTypeRepository.findById(request.typeId)
                .orElseThrow { IllegalArgumentException("project type id not found") }
        val client = clientRepository.findById(request.clientId)
                .orElseThrow { IllegalArgumentException("client id not found") }
        val manager = employeeRepository.findById(request.managerId)
                .orElseThrow { IllegalArgumentException("manager id not found") }

        return Project(
                id = request.id,
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
