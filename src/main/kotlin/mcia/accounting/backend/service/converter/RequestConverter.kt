package mcia.accounting.backend.service.converter

interface RequestConverter<out T, in R> {

    fun toEntity(request: R, id: Long = -1): T

}
