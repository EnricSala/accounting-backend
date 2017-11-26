package mcia.accounting.backend.controller.dto

import org.springframework.data.domain.Page

class PageMetadata(val page: Int,
                   val size: Int,
                   val totalElements: Long,
                   val totalPages: Int,
                   val last: Boolean) {

    companion object {

        fun of(page: Page<*>) = PageMetadata(
                page = page.number,
                size = page.size,
                totalElements = page.totalElements,
                totalPages = page.totalPages,
                last = page.isLast)

        fun just(data: List<*>) = PageMetadata(
                page = 0,
                size = data.size,
                totalElements = data.size.toLong(),
                totalPages = 1,
                last = true)

    }

}
