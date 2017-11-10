package mcia.accounting.backend.repository

import mcia.accounting.backend.entity.Client
import mcia.accounting.backend.entity.Employee
import mcia.accounting.backend.entity.Project
import mcia.accounting.backend.entity.Purchase
import mcia.accounting.backend.repository.search.SearchSpecification
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.util.*
import kotlin.reflect.KClass

class SearchSpecificationTests {

    @Test
    fun should_find_property_type() {
        String::class.matches<Purchase>("item")
        Date::class.matches<Purchase>("requestDate")
        BigDecimal::class.matches<Purchase>("amount")
        Boolean::class.matches<Project>("finished")
    }

    @Test
    fun should_find_child_property_type() {
        Client::class.matches<Purchase>("requestingProject.client")
        Employee::class.matches<Purchase>("chargingProject.manager")
    }

    @Test
    fun should_find_multiple_level_child_property_type() {
        Long::class.matches<Project>("client.type.id")
        String::class.matches<Project>("client.type.name")
        String::class.matches<Purchase>("requestingProject.manager.email")
        String::class.matches<Purchase>("chargingProject.client.type.name")
    }

    @Test(expected = IllegalArgumentException::class)
    fun fails_on_unkown_property() {
        String::class.matches<Purchase>("potato")
    }

    private inline fun <reified T : Any> KClass<*>.matches(property: String) =
            assertEquals(this, SearchSpecification.typeOf<T>(property))

}
