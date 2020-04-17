package com.np.onboarding.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.time.Instant
import javax.persistence.*
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

/**
 * The Employee entity.
 */
@Entity
@Table(name = "volunteer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "volunteer")
data class Volunteer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    /**
     * The firstname attribute.
     */
    @Column(name = "first_name")
    var firstName: String? = null,

    @Column(name = "last_name")
    var lastName: String? = null,

    @Column(name = "email")
    var email: String? = null,

    @Column(name = "phone_number")
    var phoneNumber: String? = null,

    @Column(name = "hire_date")
    var hireDate: Instant? = null,

    @OneToMany(mappedBy = "volunteer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    var opportunities: MutableSet<Opportunity> = mutableSetOf(),

    @ManyToOne @JsonIgnoreProperties("volunteers")
    var supervisor: Volunteer? = null,

    /**
     * Another side of the same relationship
     */
    @ManyToOne @JsonIgnoreProperties("volunteers")
    var organization: Organization? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addOpportunity(opportunity: Opportunity): Volunteer {
        this.opportunities.add(opportunity)
        opportunity.volunteer = this
        return this
    }

    fun removeOpportunity(opportunity: Opportunity): Volunteer {
        this.opportunities.remove(opportunity)
        opportunity.volunteer = null
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Volunteer) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Volunteer{" +
        "id=$id" +
        ", firstName='$firstName'" +
        ", lastName='$lastName'" +
        ", email='$email'" +
        ", phoneNumber='$phoneNumber'" +
        ", hireDate='$hireDate'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
