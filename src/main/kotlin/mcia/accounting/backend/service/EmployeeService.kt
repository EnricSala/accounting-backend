package mcia.accounting.backend.service

import mcia.accounting.backend.entity.Employee
import mcia.accounting.backend.repository.EmployeeRepository
import mcia.accounting.backend.service.converter.EmployeeConverter
import mcia.accounting.backend.service.request.EmployeeRequest
import org.springframework.stereotype.Service

@Service
class EmployeeService(repository: EmployeeRepository, converter: EmployeeConverter) :
        BaseService<Employee, EmployeeRequest>(Employee.RESOURCE, repository, converter)
