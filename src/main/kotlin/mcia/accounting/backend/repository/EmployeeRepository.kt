package mcia.accounting.backend.repository

import mcia.accounting.backend.entity.Employee
import org.springframework.data.repository.CrudRepository

interface EmployeeRepository : CrudRepository<Employee, Long>
