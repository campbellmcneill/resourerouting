package com.np.onboarding.domain

import java.io.Serializable
import javax.persistence.*
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

/**
 * A Country.
 */
@Entity
@Table(name = "country")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "country")
data class Country(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "country_name")
    var countryName: String? = null,

    @OneToOne @JoinColumn(unique = true)
    var region: Region? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Country) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Country{" +
        "id=$id" +
        ", countryName='$countryName'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
