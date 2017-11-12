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

        @Column var amount: BigDecimal,

        @Column var comments: String = "",

        @Column var requestDate: Date,

        @Column var invoiceFile: String? = null,

        @ManyToOne var requestingEmployee: Employee,

        @ManyToOne var requestingProject: Project,

        @ManyToOne var chargingProject: Project,

        @ManyToOne var state: PurchaseState,

        @ManyToOne var type: PurchaseType,

        @ManyToOne var supplier: Supplier)
