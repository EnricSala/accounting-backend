package mcia.accounting.backend.controller

import mcia.accounting.backend.controller.dto.ErrorMessage
import mcia.accounting.backend.service.exception.FileNotFoundException
import mcia.accounting.backend.service.exception.InvalidRequestException
import mcia.accounting.backend.service.exception.ResourceNotFoundException
import mcia.accounting.backend.utils.loggerOf
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidRequestException::class)
    fun handleInvalidRequestException(exception: InvalidRequestException): ErrorMessage {
        log.warn("Bad request: {}", exception.message)
        return ErrorMessage.badRequest(exception.message ?: DEFAULT_REASON)
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(exception: ResourceNotFoundException): ErrorMessage {
        log.warn("Resource not found: {}", exception.message)
        return ErrorMessage.notFound(exception.message ?: DEFAULT_REASON)
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FileNotFoundException::class)
    fun handleFileNotFoundException(exception: FileNotFoundException) {
        log.warn("File not found: {}", exception.message)
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(exception: RuntimeException): ErrorMessage {
        log.error("Internal error", exception)
        return ErrorMessage.internal(exception.message ?: DEFAULT_REASON)
    }

    companion object {
        private val log = loggerOf(ExceptionHandler::class)
        private const val DEFAULT_REASON = "unknown reason"

    }

}
