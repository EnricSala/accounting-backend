package mcia.accounting.backend.repository.search

enum class SearchOperation(val char: String) {

    EQUAL(":"),
    NOT_EQUAL("!"),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    CONTAINS("~"),
    UNKNOWN("?");

    companion object {
        fun from(input: String): SearchOperation =
                SearchOperation.values().firstOrNull { it.char == input } ?: UNKNOWN
    }

}
