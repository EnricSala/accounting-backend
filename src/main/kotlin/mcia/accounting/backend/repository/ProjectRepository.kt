package mcia.accounting.backend.repository

import mcia.accounting.backend.entity.Project
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository

interface ProjectRepository : CrudRepository<Project, Long>, JpaSpecificationExecutor<Project>
