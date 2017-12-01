package mcia.accounting.backend.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import javax.persistence.*

@Entity
data class AppUser(

        @GeneratedValue(strategy = GenerationType.AUTO)
        @Id var id: Long = -1,

        @Column(unique = true) var username: String,

        @JsonIgnore
        @Column var password: String,

        @Column var enabled: Boolean,

        @OneToOne var employee: Employee,

        @Enumerated(EnumType.STRING)
        @ElementCollection(targetClass = AppRole::class)
        @Fetch(FetchMode.JOIN)
        @Column var roles: MutableSet<AppRole>) {

    override fun toString(): String =
            "AppUser(id=$id, username=$username, enabled=$enabled roles=$roles)"

    companion object {
        const val RESOURCE = "user"
    }

}
