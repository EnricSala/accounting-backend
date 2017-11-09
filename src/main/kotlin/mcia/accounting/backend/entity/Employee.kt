package mcia.accounting.backend.entity

import javax.persistence.*

@Entity
data class Employee(

        @GeneratedValue(strategy = GenerationType.AUTO)
        @Id val id: Long = -1,

        @Column var fullname: String,

        @Column var email: String = "",

        @Column var comments: String = "")
