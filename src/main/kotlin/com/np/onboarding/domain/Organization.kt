package com.np.onboarding.domain

import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.*
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

/**
 * A Organization.
 */
@Entity
@Table(name = "organization")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "organization")
data class Organization(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @get: NotNull
    @Column(name = "organization_name", nullable = false)
    var organizationName: String? = null,

    @OneToOne @JoinColumn(unique = true)
    var location: Location? = null,

    @OneToOne @JoinColumn(unique = true)
    var opportunity: Opportunity? = null,

    /**
     * A relationship
     */
    @OneToMany(mappedBy = "organization")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    var volunteers: MutableSet<Volunteer> = mutableSetOf()

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addVolunteer(volunteer: Volunteer): Organization {
        this.volunteers.add(volunteer)
        volunteer.organization = this
        return this
    }

    fun removeVolunteer(volunteer: Volunteer): Organization {
        this.volunteers.remove(volunteer)
        volunteer.organization = null
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Organization) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Organization{" +
        "id=$id" +
        ", organizationName='$organizationName'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
