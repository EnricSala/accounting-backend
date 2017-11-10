package mcia.accounting.backend.controller

import mcia.accounting.backend.config.WebConfig
import mcia.accounting.backend.entity.Employee
import mcia.accounting.backend.repository.EmployeeRepository
import mcia.accounting.backend.utils.loggerOf
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(EmployeeController.PATH)
class EmployeeController(private val employeeRepository: EmployeeRepository) {

    @GetMapping
    fun findAll(): Iterable<Employee> {
        log.info("GET $PATH")
        return employeeRepository.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): Employee {
        log.info("GET $PATH/$id")
        return employeeRepository.findById(id)
                .orElseThrow { RuntimeException("not found") }
    }

    @PostMapping
    fun create(@RequestBody employee: Employee): Employee {
        log.info("POST $PATH $employee")
        if (employee.id < 0)
            throw RuntimeException("insert cannot set the id")
        return employeeRepository.save(employee)
    }

    @PutMapping
    fun update(@RequestBody employee: Employee): Employee {
        log.info("PUT $PATH $employee")
        if (!employeeRepository.existsById(employee.id))
            throw RuntimeException("id not found")
        return employeeRepository.save(employee)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        log.info("DELETE $PATH/$id")
        return employeeRepository.deleteById(id)
    }

    companion object {
        internal const val PATH = "${WebConfig.BASE_API_PATH}/employee"
        private val log = loggerOf(EmployeeController::class)
    }

}
