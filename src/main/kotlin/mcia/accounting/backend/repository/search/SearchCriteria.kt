package mcia.accounting.backend.repository.search

import java.util.*

data class SearchCriteria(val key: String,
                          val operation: SearchOperation,
                          val value: String) {

    fun date(): Date = Date(this.value.toLong())

    fun boolean(): Boolean = this.value.toBoolean()

    companion object {
        private const val QUERY_PATTERN = "(.+?)([:<>!~])(.+?)(?:,|$)"
        private val QUERY_REGEX = Regex(QUERY_PATTERN)

        fun of(key: String, operation: String, value: String): SearchCriteria {
            val specOperation = SearchOperation.from(operation)
            return SearchCriteria(key, specOperation, value)
        }

        fun from(query: String): List<SearchCriteria> {
            return QUERY_REGEX.findAll(query)
                    .map { it.groupValues }
                    .map { SearchCriteria.of(it[1], it[2], it[3]) }
                    .filter { it.operation != SearchOperation.UNKNOWN }
                    .toList()
        }
    }

}
