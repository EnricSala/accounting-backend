package mcia.accounting.backend.repository

import mcia.accounting.backend.entity.ProjectType
import org.springframework.data.repository.CrudRepository

interface ProjectTypeRepository : CrudRepository<ProjectType, Long>
