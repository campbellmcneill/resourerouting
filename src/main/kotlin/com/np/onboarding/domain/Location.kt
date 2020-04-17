package com.np.onboarding.domain

import java.io.Serializable
import javax.persistence.*
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

/**
 * not an ignored comment
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "location")
data class Location(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "street_address")
    var streetAddress: String? = null,

    @Column(name = "postal_code")
    var postalCode: String? = null,

    @Column(name = "city")
    var city: String? = null,

    @Column(name = "state_province")
    var stateProvince: String? = null,

    @OneToOne @JoinColumn(unique = true)
    var country: Country? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Location) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Location{" +
        "id=$id" +
        ", streetAddress='$streetAddress'" +
        ", postalCode='$postalCode'" +
        ", city='$city'" +
        ", stateProvince='$stateProvince'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
