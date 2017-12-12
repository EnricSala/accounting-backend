package mcia.accounting.backend.controller.dto

import org.springframework.data.domain.Page

class PageResult<out T>(val metadata: PageMetadata,
                        val content: Iterable<T>) : Iterable<T> {

    override fun iterator(): Iterator<T> = content.iterator()

    companion object {
        fun <T> of(page: Page<T>) = PageResult(PageMetadata.of(page), page.content)
        fun <T> just(data: List<T>) = PageResult(PageMetadata.just(data), data)
        fun <T> empty(): PageResult<T> = just(listOf())
    }

}
