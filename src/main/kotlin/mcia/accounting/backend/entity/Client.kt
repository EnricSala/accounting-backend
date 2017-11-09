package mcia.accounting.backend.entity

import javax.persistence.*

@Entity
data class Client(

        @GeneratedValue(strategy = GenerationType.AUTO)
        @Id val id: Long = -1,

        @Column var name: String,

        @Column var acronym: String,

        @ManyToOne var type: ClientType)
