package mcia.accounting.backend.controller.dto

class ErrorMessage(val type: String, val message: String) {

    companion object {
        fun badRequest(message: String) = ErrorMessage("invalid request", message)
        fun unauthorized(message: String) = ErrorMessage("unauthorized", message)
        fun conflict(message: String) = ErrorMessage("conflict", message)
        fun notFound(message: String) = ErrorMessage("not found", message)
        fun internal(message: String) = ErrorMessage("internal error", message)
    }

}
