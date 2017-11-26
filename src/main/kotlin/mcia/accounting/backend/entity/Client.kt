package mcia.accounting.backend.entity

import javax.persistence.*

@Entity
data class Client(

        @GeneratedValue(strategy = GenerationType.AUTO)
        @Id var id: Long = -1,

        @Column var name: String,

        @Column var acronym: String,

        @ManyToOne var type: ClientType) {

    companion object {
        const val RESOURCE = "client"
    }

}
