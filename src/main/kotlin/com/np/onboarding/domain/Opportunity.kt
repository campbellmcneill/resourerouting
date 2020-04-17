package com.np.onboarding.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import javax.persistence.*
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

/**
 * A Opportunity.
 */
@Entity
@Table(name = "opportunity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "opportunity")
data class Opportunity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "opportunity_title")
    var opportunityTitle: String? = null,

    @Column(name = "opportunity_description")
    var opportunityDescription: String? = null,

    @Column(name = "weekly_time_commitment")
    var weeklyTimeCommitment: Long? = null,

    @Column(name = "duration")
    var duration: Long? = null,

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "opportunity_task",
        joinColumns = [JoinColumn(name = "opportunity_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "task_id", referencedColumnName = "id")])
    var tasks: MutableSet<Task> = mutableSetOf(),

    @ManyToOne @JsonIgnoreProperties("opportunities")
    var volunteer: Volunteer? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addTask(task: Task): Opportunity {
        this.tasks.add(task)
        return this
    }

    fun removeTask(task: Task): Opportunity {
        this.tasks.remove(task)
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Opportunity) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Opportunity{" +
        "id=$id" +
        ", opportunityTitle='$opportunityTitle'" +
        ", opportunityDescription='$opportunityDescription'" +
        ", weeklyTimeCommitment=$weeklyTimeCommitment" +
        ", duration=$duration" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
