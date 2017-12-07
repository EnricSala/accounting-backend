package mcia.accounting.backend.repository

import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.PagingAndSortingRepository

@NoRepositoryBean
interface SearchRepository<T> : PagingAndSortingRepository<T, Long>, JpaSpecificationExecutor<T>
