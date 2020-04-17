package com.np.onboarding.service.dto

import java.io.Serializable

/**
 * A DTO for the [com.np.onboarding.domain.Opportunity] entity.
 */
data class OpportunityDTO(

    var id: Long? = null,

    var opportunityTitle: String? = null,

    var opportunityDescription: String? = null,

    var weeklyTimeCommitment: Long? = null,

    var duration: Long? = null,

    var tasks: MutableSet<TaskDTO> = mutableSetOf(),

    var volunteerId: Long? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OpportunityDTO) return false
        val opportunityDTO = other as OpportunityDTO
        if (opportunityDTO.id == null || id == null) {
            return false
        }
        return id == opportunityDTO.id
    }

    override fun hashCode() = id.hashCode()
}
