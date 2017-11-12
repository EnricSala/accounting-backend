package mcia.accounting.backend.entity

import javax.persistence.*

@Entity
data class ProjectType(

        @GeneratedValue(strategy = GenerationType.AUTO)
        @Id var id: Long = -1,

        @Column var name: String)
