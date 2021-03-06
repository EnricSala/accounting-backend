package mcia.accounting.backend.entity

import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity
data class Purchase(

        @GeneratedValue(strategy = GenerationType.AUTO)
        @Id var id: Long = -1,

        @Column var item: String,

        @Column(unique = true) var code: String,

        @Column var engagement: String,

        @Column var billing: String,

        @Column var codeERP: String,

        @Column var codeRP: String,

        @Column var codeLV: String,

        @Column var amount: BigDecimal,

        @Column var comments: String,

        @Column var requestDate: Date,

        @Column var invoicePath: String?,

        @ManyToOne var requestingEmployee: Employee,

        @ManyToOne var requestingProject: Project,

        @ManyToOne var chargingProject: Project,

        @ManyToOne var state: PurchaseState,

        @ManyToOne var type: PurchaseType,

        @ManyToOne var supplier: Supplier) {

    companion object {
        const val RESOURCE = "purchase"
    }

}
