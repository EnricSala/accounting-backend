package mcia.accounting.backend.repository

import mcia.accounting.backend.repository.search.SearchCriteria
import mcia.accounting.backend.repository.search.SearchOperation.*
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchCriteriaTests {

    @Test
    fun should_find_single_criteria() {
        val query = "name:enric"
        val criteria = SearchCriteria.from(query)
        val expected = listOf(SearchCriteria("name", EQUAL, "enric"))
        assertEquals(expected, criteria)
    }

    @Test
    fun should_find_multiple_criteria() {
        val query = "name~enric,title:doctor,amount>123.45"
        val criteria = SearchCriteria.from(query)
        val expected = listOf(
                SearchCriteria("name", CONTAINS, "enric"),
                SearchCriteria("title", EQUAL, "doctor"),
                SearchCriteria("amount", GREATER_THAN, "123.45"))
        assertEquals(expected, criteria)
    }

}
