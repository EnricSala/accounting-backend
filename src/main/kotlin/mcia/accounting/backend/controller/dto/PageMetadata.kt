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
    }

}
