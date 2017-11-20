package mcia.accounting.backend.repository

import mcia.accounting.backend.entity.*
import mcia.accounting.backend.repository.search.SearchSpecification
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

@RunWith(SpringRunner::class)
@DataJpaTest
class DynamicQueryTests {

    @Autowired private lateinit var purchaseRepository: PurchaseRepository
    @Autowired private lateinit var purchaseTypeRepository: PurchaseTypeRepository
    @Autowired private lateinit var purchaseStateRepository: PurchaseStateRepository
    @Autowired private lateinit var projectRepository: ProjectRepository
    @Autowired private lateinit var projectTypeRepository: ProjectTypeRepository
    @Autowired private lateinit var supplierRepository: SupplierRepository
    @Autowired private lateinit var employeeRepository: EmployeeRepository
    @Autowired private lateinit var clientRepository: ClientRepository
    @Autowired private lateinit var clientTypeRepository: ClientTypeRepository

    @Before
    fun setup() {
        val employees = employeeRepository.saveAll(listOf(
                Employee(fullname = "John Snow", email = "", comments = ""),
                Employee(fullname = "Daenerys Targaryen", email = "", comments = ""),
                Employee(fullname = "Tyrion Lannister", email = "", comments = ""),
                Employee(fullname = "Arya Stark", email = "", comments = ""),
                Employee(fullname = "Margaery Tyrell", email = "", comments = ""))).toList()

        val clientTypes = clientTypeRepository.saveAll(listOf(
                ClientType(name = "Private"),
                ClientType(name = "Public Local"),
                ClientType(name = "Public Europe"))).toList()

        val clients = clientRepository.saveAll(listOf(
                Client(name = "Google", acronym = "GOOGL", type = clientTypes[0]),
                Client(name = "Amazon", acronym = "AMZN", type = clientTypes[0]),
                Client(name = "Microsoft", acronym = "MSFT", type = clientTypes[1]),
                Client(name = "Apple", acronym = "AAPL", type = clientTypes[2]))).toList()

        val projectTypes = projectTypeRepository.saveAll(listOf(
                ProjectType(name = "Service"),
                ProjectType(name = "Research"))).toList()

        val projects = projectRepository.saveAll(listOf(
                Project(name = "Moscosinus", client = clients[0], manager = employees[0],
                        code = "M-1234", type = projectTypes[0], finished = true),
                Project(name = "YellowMaps", client = clients[0], manager = employees[0],
                        code = "Y-1234", type = projectTypes[0], finished = true),
                Project(name = "IMPACKTO", client = clients[1], manager = employees[1],
                        code = "I-1234", type = projectTypes[1], finished = false),
                Project(name = "MegaNergest", client = clients[2], manager = employees[2],
                        code = "M-2345", type = projectTypes[1], finished = false),
                Project(name = "CleverHook", client = clients[3], manager = employees[3],
                        code = "S-1234", type = projectTypes[1], finished = false))).toList()

        val suppliers = supplierRepository.saveAll(listOf(
                Supplier(name = "Amazon"),
                Supplier(name = "Farnell"),
                Supplier(name = "RS"))).toList()

        val purchaseStates = purchaseStateRepository.saveAll(listOf(
                PurchaseState(name = "Pending"),
                PurchaseState(name = "Done"),
                PurchaseState(name = "Cancelled"))).toList()

        val purchaseTypes = purchaseTypeRepository.saveAll(listOf(
                PurchaseType(name = "Equipment"),
                PurchaseType(name = "Advertisment"),
                PurchaseType(name = "Other"))).toList()

        purchaseRepository.saveAll(listOf(
                Purchase(item = "Samsung Galaxy S3", amount = BigDecimal("100.25"),
                        requestingProject = projects[0], chargingProject = projects[0],
                        requestDate = "2017-05-23".date(), requestingEmployee = employees[0],
                        supplier = suppliers[0], code = "SGS3-1234",
                        state = purchaseStates[0], type = purchaseTypes[1],
                        invoicePath = null, comments = ""),
                Purchase(item = "Motorola G2 Plus", amount = BigDecimal("200.50"),
                        requestingProject = projects[0], chargingProject = projects[1],
                        requestDate = "2017-08-15".date(), requestingEmployee = employees[0],
                        supplier = suppliers[1], code = "MGSP-2345",
                        state = purchaseStates[1], type = purchaseTypes[2],
                        invoicePath = null, comments = ""),
                Purchase(item = "HP Z420 Server", amount = BigDecimal("300.75"),
                        requestingProject = projects[1], chargingProject = projects[2],
                        requestDate = "2017-11-04".date(), requestingEmployee = employees[1],
                        supplier = suppliers[2], code = "HPZS-3456",
                        state = purchaseStates[2], type = purchaseTypes[0],
                        invoicePath = null, comments = "")))
    }

    @After
    fun cleanup() {
        purchaseRepository.deleteAll()
        purchaseTypeRepository.deleteAll()
        purchaseStateRepository.deleteAll()
        projectRepository.deleteAll()
        projectTypeRepository.deleteAll()
        supplierRepository.deleteAll()
        employeeRepository.deleteAll()
        clientRepository.deleteAll()
        clientTypeRepository.deleteAll()
    }

    @Test
    fun empty_query_should_find_everything() {
        val query = ""
        val spec = SearchSpecification.from<Purchase>(query)
        assertThat(purchaseRepository.findAll(spec)).hasSize(3)
    }

    @Test
    fun should_find_by_numeric_property() {
        val query = "amount>150.00"
        val spec = SearchSpecification.from<Purchase>(query)
        assertThat(purchaseRepository.findAll(spec))
                .hasSize(2)
                .allMatch { it.amount > BigDecimal("150.00") }
    }

    @Test
    fun should_find_by_child_property() {
        val query = "requestingProject.name~ello"
        val spec = SearchSpecification.from<Purchase>(query)
        assertThat(purchaseRepository.findAll(spec))
                .hasSize(1)
                .allMatch { it.requestingProject.name.contains("ello", true) }
    }

    @Test
    fun should_find_by_multiple_child_property() {
        val query = "chargingProject.manager.fullname~aene"
        val spec = SearchSpecification.from<Purchase>(query)
        assertThat(purchaseRepository.findAll(spec))
                .hasSize(1)
                .allMatch { it.chargingProject.manager.fullname.contains("aene", true) }
    }

    @Test
    fun should_find_by_date_property() {
        val date = "2017-09-01".date()

        val queryBefore = "requestDate<${date.time}"
        val specBefore = SearchSpecification.from<Purchase>(queryBefore)
        assertThat(purchaseRepository.findAll(specBefore))
                .hasSize(2)
                .allMatch { it.requestDate < date }

        val queryAfter = "requestDate>${date.time}"
        val specAfter = SearchSpecification.from<Purchase>(queryAfter)
        assertThat(purchaseRepository.findAll(specAfter))
                .hasSize(1)
                .allMatch { it.requestDate > date }
    }

    @Test
    fun should_find_by_boolean_property() {
        val finishedQuery = "chargingProject.finished:true"
        val finishedSpec = SearchSpecification.from<Purchase>(finishedQuery)
        assertThat(purchaseRepository.findAll(finishedSpec))
                .hasSize(2)
                .allMatch { it.chargingProject.finished }

        val notFinishedQuery = "chargingProject.finished:false"
        val notFinishedSpec = SearchSpecification.from<Purchase>(notFinishedQuery)
        assertThat(purchaseRepository.findAll(notFinishedSpec))
                .hasSize(1)
                .allMatch { !it.chargingProject.finished }
    }

    @Test
    fun should_find_by_multiple_property() {
        val query = "amount<250.33,supplier.name~azon"
        val spec = SearchSpecification.from<Purchase>(query)
        assertThat(purchaseRepository.findAll(spec))
                .hasSize(1)
                .allMatch { it.amount < BigDecimal("250.33") }
                .allMatch { it.supplier.name.contains("azon", true) }
    }

    private val formatter = SimpleDateFormat("yyyy-MM-dd")
    private fun String.date(): Date = formatter.parse(this)

}
