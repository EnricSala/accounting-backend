package mcia.accounting.backend.entity

import javax.persistence.*

@Entity
data class Project(

        @GeneratedValue(strategy = GenerationType.AUTO)
        @Id var id: Long = -1,

        @Column var name: String,

        @Column var description: String = "",

        @Column var code: String,

        @Column var finished: Boolean = false,

        @ManyToOne var manager: Employee,

        @ManyToOne var client: Client,

        @ManyToOne var type: ProjectType) {

    companion object {
        const val RESOURCE = "project"
    }

}
