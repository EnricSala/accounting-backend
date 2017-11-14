package mcia.accounting.backend.repository.search

import mcia.accounting.backend.repository.search.SearchOperation.*
import mcia.accounting.backend.service.exception.InvalidRequestException
import org.springframework.data.jpa.domain.Specification
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Path
import javax.persistence.criteria.Root
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.jvmErasure

object SearchSpecification {

    const val PROPERTY_DELIMITER = '.'

    fun <T> matchAll(): Specification<T> = Specification { _, _, _ -> null }

    inline fun <reified T : Any> from(query: String): Specification<T> {
        if (query.isBlank()) return matchAll()
        val criteria = SearchCriteria.from(query)
        if (criteria.isEmpty())
            throw InvalidRequestException("invalid query: $query")
        return criteria
                .map { of<T>(it) }
                .reduce { acc, next -> Specification.where(acc).and(next) }
    }

    inline fun <reified T : Any> of(criteria: SearchCriteria): Specification<T> =
            Specification { root: Root<T>, _: CriteriaQuery<*>, builder: CriteriaBuilder ->
                val type = typeOf<T>(criteria.key)
                val (prop, path) = findLastProperty(criteria.key, root)
                when (criteria.operation) {
                    EQUAL -> when (type) {
                        String::class -> builder.equal(path.get<String>(prop), criteria.value)
                        Date::class -> builder.equal(path.get<Date>(prop), criteria.date())
                        Boolean::class -> builder.equal(path.get<Boolean>(prop), criteria.boolean())
                        else -> builder.equal(path.get<String>(prop), criteria.value)
                    }
                    NOT_EQUAL -> when (type) {
                        String::class -> builder.notEqual(path.get<String>(prop), criteria.value)
                        Date::class -> builder.notEqual(path.get<Date>(prop), criteria.date())
                        Boolean::class -> builder.notEqual(path.get<Boolean>(prop), criteria.boolean())
                        else -> builder.notEqual(path.get<String>(prop), criteria.value)
                    }
                    GREATER_THAN -> when (type) {
                        String::class -> builder.greaterThanOrEqualTo(path.get<String>(prop), criteria.value)
                        Date::class -> builder.greaterThanOrEqualTo(path.get<Date>(prop), criteria.date())
                        Boolean::class -> throw InvalidRequestException("greater_than does not support $type")
                        else -> builder.greaterThanOrEqualTo(path.get<String>(prop), criteria.value)
                    }
                    LESS_THAN -> when (type) {
                        String::class -> builder.lessThanOrEqualTo(path.get<String>(prop), criteria.value)
                        Date::class -> builder.lessThanOrEqualTo(path.get<Date>(prop), criteria.date())
                        Boolean::class -> throw InvalidRequestException("less_than does not support $type")
                        else -> builder.lessThanOrEqualTo(path.get<String>(prop), criteria.value)
                    }
                    CONTAINS -> when (type) {
                        String::class -> {
                            val upperCase = builder.upper(path.get<String>(prop))
                            builder.like(upperCase, "%${criteria.value.toUpperCase()}%")
                        }
                        else -> throw InvalidRequestException("contains does not support $type")
                    }
                    else -> throw InvalidRequestException("unsupported search operation")
                }
            }

    inline fun <reified T : Any> typeOf(property: String): KClass<*> {
        var type: KClass<*> = T::class
        for (part in property.split(PROPERTY_DELIMITER)) {
            val props = type.declaredMemberProperties
            type = props.firstOrNull { it.name == part }?.returnType?.jvmErasure
                    ?: throw InvalidRequestException("unknown property $property")
        }
        return type
    }

    fun findLastProperty(key: String, root: Root<*>): Pair<String, Path<*>> {
        val parts = key.split(PROPERTY_DELIMITER)
        var path: Path<*> = root
        for (i in 0..(parts.size - 2))
            path = path.get<Any>(parts[i])
        return parts.last() to path
    }

}
