package com.np.onboarding.domain

import com.np.onboarding.domain.enumeration.Language
import java.io.Serializable
import java.time.Instant
import javax.persistence.*
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

/**
 * A OpportunityHistory.
 */
@Entity
@Table(name = "opportunity_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "opportunityhistory")
data class OpportunityHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "start_date")
    var startDate: Instant? = null,

    @Column(name = "end_date")
    var endDate: Instant? = null,

    @Column(name = "rating")
    var rating: Long? = null,

    @Column(name = "comments")
    var comments: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    var language: Language? = null,

    @OneToOne @JoinColumn(unique = true)
    var opportunity: Opportunity? = null,

    @OneToOne @JoinColumn(unique = true)
    var organization: Organization? = null,

    @OneToOne @JoinColumn(unique = true)
    var volunteer: Volunteer? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OpportunityHistory) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "OpportunityHistory{" +
        "id=$id" +
        ", startDate='$startDate'" +
        ", endDate='$endDate'" +
        ", rating=$rating" +
        ", comments='$comments'" +
        ", language='$language'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
