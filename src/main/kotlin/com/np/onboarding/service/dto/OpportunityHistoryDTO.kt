package com.np.onboarding.service.dto

import com.np.onboarding.domain.enumeration.Language
import java.io.Serializable
import java.time.Instant

/**
 * A DTO for the [com.np.onboarding.domain.OpportunityHistory] entity.
 */
data class OpportunityHistoryDTO(

    var id: Long? = null,

    var startDate: Instant? = null,

    var endDate: Instant? = null,

    var rating: Long? = null,

    var comments: String? = null,

    var language: Language? = null,

    var opportunityId: Long? = null,

    var organizationId: Long? = null,

    var volunteerId: Long? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OpportunityHistoryDTO) return false
        val opportunityHistoryDTO = other as OpportunityHistoryDTO
        if (opportunityHistoryDTO.id == null || id == null) {
            return false
        }
        return id == opportunityHistoryDTO.id
    }

    override fun hashCode() = id.hashCode()
}
