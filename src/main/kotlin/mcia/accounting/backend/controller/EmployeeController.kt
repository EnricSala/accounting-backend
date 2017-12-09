package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.Employee
import mcia.accounting.backend.service.EmployeeService
import mcia.accounting.backend.service.request.EmployeeRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(EmployeeController.PATH)
class EmployeeController(service: EmployeeService) :
        BaseController<Employee, EmployeeRequest>(PATH, service) {

    @GetMapping
    fun findAll(): Iterable<Employee> {
        log.debug("GET {}", PATH)
        return service.findAll()
    }

    companion object {
        const val PATH = WebConfig.BASE_API_PATH + "/" + Employee.RESOURCE
    }

}
