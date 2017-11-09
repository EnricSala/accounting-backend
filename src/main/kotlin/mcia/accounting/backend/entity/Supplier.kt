package mcia.accounting.backend.entity

import javax.persistence.*

@Entity
data class Supplier(

        @GeneratedValue(strategy = GenerationType.AUTO)
        @Id val id: Long = -1,

        @Column var name: String)
