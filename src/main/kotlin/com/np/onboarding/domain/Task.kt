package com.np.onboarding.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import javax.persistence.*
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

/**
 * Task entity.\n@author The JHipster team.
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "task")
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "title")
    var title: String? = null,

    @Column(name = "description")
    var description: String? = null,

    @ManyToMany(mappedBy = "tasks")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    var jobs: MutableSet<Opportunity> = mutableSetOf()

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addJob(opportunity: Opportunity): Task {
        this.jobs.add(opportunity)
        opportunity.tasks.add(this)
        return this
    }

    fun removeJob(opportunity: Opportunity): Task {
        this.jobs.remove(opportunity)
        opportunity.tasks.remove(this)
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Task) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Task{" +
        "id=$id" +
        ", title='$title'" +
        ", description='$description'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
