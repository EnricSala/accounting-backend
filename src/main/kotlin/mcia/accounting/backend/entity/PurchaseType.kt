package mcia.accounting.backend.entity

import javax.persistence.*

@Entity
data class PurchaseType(

        @GeneratedValue(strategy = GenerationType.AUTO)
        @Id var id: Long = -1,

        @Column var name: String) {

    companion object {
        const val RESOURCE = "purchase-type"
    }

}
