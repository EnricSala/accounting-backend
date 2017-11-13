package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.Employee
import mcia.accounting.backend.repository.EmployeeRepository
import mcia.accounting.backend.service.exception.InvalidRequestException
import mcia.accounting.backend.service.exception.ResourceNotFoundException
import mcia.accounting.backend.utils.loggerOf
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(EmployeeController.PATH)
class EmployeeController(private val employeeRepository: EmployeeRepository) {

    @GetMapping
    fun findAll(): Iterable<Employee> {
        log.debug("GET {}", PATH)
        return employeeRepository.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): Employee {
        log.debug("GET {}/{}", PATH, id)
        return employeeRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("employee id not found") }
    }

    @PostMapping
    fun create(@RequestBody employee: Employee): Employee {
        log.debug("POST {} {}", PATH, employee)
        if (employee.id < 0)
            return employeeRepository.save(employee)
        else
            throw InvalidRequestException("insert cannot set the id")
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody employee: Employee): Employee {
        log.debug("PUT {}/{} {}", PATH, id, employee)
        if (employeeRepository.existsById(id)) {
            employee.id = id
            return employeeRepository.save(employee)
        } else
            throw ResourceNotFoundException("employee id not found")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        log.debug("DELETE {}/{}", PATH, id)
        return employeeRepository.deleteById(id)
    }

    companion object {
        internal const val PATH = "${WebConfig.BASE_API_PATH}/employee"
        private val log = loggerOf(EmployeeController::class)
    }

}
