package mcia.accounting.backend.repository.search

import mcia.accounting.backend.repository.search.SearchOperation.*
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.*

class SearchSpecification<T>(private val criteria: SearchCriteria) : Specification<T> {

    // FIXME: fails for Date or Boolean fields
    override fun toPredicate(root: Root<T>,
                             query: CriteriaQuery<*>,
                             builder: CriteriaBuilder): Predicate {
        val path = unpack(root)
        return when (criteria.operation) {
            EQUAL -> builder.equal(path, criteria.value)
            NOT_EQUAL -> builder.notEqual(path, criteria.value)
            GREATER_THAN -> builder.greaterThanOrEqualTo(path, criteria.value)
            LESS_THAN -> builder.lessThanOrEqualTo(path, criteria.value)
            CONTAINS -> builder.like(builder.upper(path), "%${criteria.value.toUpperCase()}%")
            else -> throw IllegalArgumentException("unknown search operation")
        }
    }

    private fun unpack(root: Root<T>): Path<String> {
        return if (!criteria.key.contains('.'))
            root.get<String>(criteria.key)
        else {
            var result: Path<String>? = null
            criteria.key.split('.')
                    .forEach { result = (result ?: root).get<String>(it) }
            result!!
        }
    }

    companion object {
        private const val QUERY_PATTERN = "(.+?)([:<>!~])(.+?),"
        private val queryRegex = Regex(QUERY_PATTERN)

        // TODO: find a way to remove the extra comma
        fun <T> from(query: String): Specification<T> {
            return if (query.isBlank())
                Specification { _, _, _ -> null }
            else {
                try {
                    queryRegex.findAll("$query,")
                            .map { it.groupValues }
                            .map { SearchCriteria.of(it[1], it[2], it[3]) }
                            .filter { it.operation != SearchOperation.UNKNOWN }
                            .map { SearchSpecification<T>(it) as Specification<T> }
                            .reduce { acc, next -> Specification.where(acc).and(next) }
                } catch (e: UnsupportedOperationException) {
                    throw IllegalArgumentException("invalid query", e)
                }
            }
        }
    }

}
