package com.np.onboarding.service.dto

import java.io.Serializable
import javax.validation.constraints.NotNull

/**
 * A DTO for the [com.np.onboarding.domain.Organization] entity.
 */
data class OrganizationDTO(

    var id: Long? = null,

    @get: NotNull
    var organizationName: String? = null,

    var locationId: Long? = null,

    var opportunityId: Long? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OrganizationDTO) return false
        val organizationDTO = other as OrganizationDTO
        if (organizationDTO.id == null || id == null) {
            return false
        }
        return id == organizationDTO.id
    }

    override fun hashCode() = id.hashCode()
}
