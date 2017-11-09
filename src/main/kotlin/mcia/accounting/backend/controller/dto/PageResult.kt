package mcia.accounting.backend.controller.dto

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

class PageResult<out T>(val metadata: PageMetadata,
                        val content: Iterable<T>) {

    companion object {
        fun <T> of(page: Page<T>) = PageResult<T>(PageMetadata.of(page), page.content)
        fun <T> empty(): PageResult<T> = of(PageImpl<T>(listOf()))
    }

}
