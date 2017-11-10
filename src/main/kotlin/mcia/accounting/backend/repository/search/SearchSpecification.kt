package mcia.accounting.backend.repository.search

import mcia.accounting.backend.repository.search.SearchOperation.*
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
        try {
            return SearchCriteria.from(query)
                    .map { of<T>(it) }
                    .reduce { acc, next -> Specification.where(acc).and(next) }
        } catch (e: UnsupportedOperationException) {
            throw IllegalArgumentException("invalid query: $query", e)
        }
    }

    inline fun <reified T : Any> of(criteria: SearchCriteria): Specification<T> =
            Specification { root: Root<T>, _: CriteriaQuery<*>, builder: CriteriaBuilder ->
                val type = typeOf<T>(criteria.key)
                val path = unpackLast(root, criteria.key)
                val prop = lastProperty(criteria.key)
                when (criteria.operation) {
                    EQUAL -> when (type) {
                        String::class -> builder.equal(path.get<String>(prop), criteria.value)
                        Date::class -> throw IllegalArgumentException("dates do not support equal")
                        Boolean::class -> builder.equal(path.get<Boolean>(prop), criteria.boolean())
                        else -> builder.equal(path.get<String>(prop), criteria.value)
                    }
                    NOT_EQUAL -> when (type) {
                        String::class -> builder.notEqual(path.get<String>(prop), criteria.value)
                        Date::class -> throw IllegalArgumentException("dates do not support not_equal")
                        Boolean::class -> builder.notEqual(path.get<Boolean>(prop), criteria.boolean())
                        else -> builder.notEqual(path.get<String>(prop), criteria.value)
                    }
                    GREATER_THAN -> when (type) {
                        String::class -> builder.greaterThanOrEqualTo(path.get<String>(prop), criteria.value)
                        Date::class -> builder.greaterThanOrEqualTo(path.get<Date>(prop), criteria.date())
                        Boolean::class -> throw IllegalArgumentException("booleans do not support greater_than")
                        else -> builder.greaterThanOrEqualTo(path.get<String>(prop), criteria.value)
                    }
                    LESS_THAN -> when (type) {
                        String::class -> builder.lessThanOrEqualTo(path.get<String>(prop), criteria.value)
                        Date::class -> builder.lessThanOrEqualTo(path.get<Date>(prop), criteria.date())
                        Boolean::class -> throw IllegalArgumentException("booleans do not support less_than")
                        else -> builder.lessThanOrEqualTo(path.get<String>(prop), criteria.value)
                    }
                    CONTAINS -> when (type) {
                        String::class -> {
                            val upperCase = builder.upper(path.get<String>(prop))
                            builder.like(upperCase, "%${criteria.value.toUpperCase()}%")
                        }
                        else -> throw IllegalArgumentException("contains can only be used with strings")
                    }
                    else -> throw UnsupportedOperationException("unsupported search operation")
                }
            }

    inline fun <reified T : Any> typeOf(property: String): KClass<*> {
        var type: KClass<*> = T::class
        for (part in property.split(PROPERTY_DELIMITER)) {
            val props = type.declaredMemberProperties
            type = props.firstOrNull { it.name == part }?.returnType?.jvmErasure
                    ?: throw IllegalArgumentException("unknown property $property")
        }
        return type
    }

    fun <T> unpackLast(root: Root<T>, key: String): Path<T> {
        if (!key.contains(PROPERTY_DELIMITER))
            return root
        var result: Path<T>? = null
        val parts = key.split(PROPERTY_DELIMITER)
        for (i in 0..(parts.size - 2))
            result = (result ?: root).get<T>(parts[i])
        return result!!
    }

    fun lastProperty(key: String) = key.substringAfterLast(PROPERTY_DELIMITER)

}
