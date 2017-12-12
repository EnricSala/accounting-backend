package mcia.accounting.backend.utils

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

fun Pageable.withDefaultSort(defaultSort: Sort): Pageable = when {
    sort.isSorted -> this
    else -> PageRequest.of(pageNumber, pageSize, defaultSort)
}
