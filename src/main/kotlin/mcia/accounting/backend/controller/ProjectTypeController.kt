package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.ProjectType
import mcia.accounting.backend.repository.ProjectTypeRepository
import mcia.accounting.backend.utils.loggerOf
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(ProjectTypeController.PATH)
class ProjectTypeController(private val projectTypeRepository: ProjectTypeRepository) {

    @GetMapping
    fun findAll(): Iterable<ProjectType> {
        log.info("GET $PATH")
        return projectTypeRepository.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ProjectType {
        log.info("GET $PATH/$id")
        return projectTypeRepository.findById(id)
                .orElseThrow { RuntimeException("not found") }
    }

    @PostMapping
    fun create(@RequestBody projectType: ProjectType): ProjectType {
        log.info("POST $PATH $projectType")
        if (projectType.id < 0)
            throw RuntimeException("insert cannot set the id")
        return projectTypeRepository.save(projectType)
    }

    @PutMapping
    fun update(@RequestBody projectType: ProjectType): ProjectType {
        log.info("PUT $PATH $projectType")
        if (!projectTypeRepository.existsById(projectType.id))
            throw RuntimeException("id not found")
        return projectTypeRepository.save(projectType)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        log.info("DELETE $PATH/$id")
        return projectTypeRepository.deleteById(id)
    }

    companion object {
        internal const val PATH = "${WebConfig.BASE_API_PATH}/project-type"
        private val log = loggerOf(ProjectTypeController::class)
    }

}
