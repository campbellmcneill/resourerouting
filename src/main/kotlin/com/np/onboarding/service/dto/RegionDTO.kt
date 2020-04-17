package com.np.onboarding.service.dto

import java.io.Serializable

/**
 * A DTO for the [com.np.onboarding.domain.Region] entity.
 */
data class RegionDTO(

    var id: Long? = null,

    var regionName: String? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RegionDTO) return false
        val regionDTO = other as RegionDTO
        if (regionDTO.id == null || id == null) {
            return false
        }
        return id == regionDTO.id
    }

    override fun hashCode() = id.hashCode()
}
