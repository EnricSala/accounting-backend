package mcia.accounting.backend.entity

import javax.persistence.*

@Entity
data class PurchaseState(

        @GeneratedValue(strategy = GenerationType.AUTO)
        @Id var id: Long = -1,

        @Column var name: String,

        @Column var color: String) {

    companion object {
        const val RESOURCE = "purchase-state"
    }

}
