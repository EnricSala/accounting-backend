package mcia.accounting.backend.service.converter

import mcia.accounting.backend.entity.Employee
import mcia.accounting.backend.service.request.EmployeeRequest
import org.springframework.stereotype.Component

@Component
class EmployeeConverter : RequestConverter<Employee, EmployeeRequest> {

    override fun toEntity(request: EmployeeRequest, id: Long): Employee =
            Employee(
                    id = id,
                    fullname = request.fullname,
                    email = request.email,
                    comments = request.comments)

}
