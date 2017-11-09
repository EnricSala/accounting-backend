package mcia.accounting.backend.repository.search

data class SearchCriteria(val key: String,
                          val operation: SearchOperation,
                          val value: String) {

    companion object {
        fun of(key: String, operation: String, value: String): SearchCriteria {
            val specOperation = SearchOperation.from(operation)
            return SearchCriteria(key, specOperation, value)
        }
    }

}
