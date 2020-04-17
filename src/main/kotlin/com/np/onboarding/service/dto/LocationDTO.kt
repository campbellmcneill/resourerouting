package com.np.onboarding.service.dto

import io.swagger.annotations.ApiModel
import java.io.Serializable

/**
 * A DTO for the [com.np.onboarding.domain.Location] entity.
 */
@ApiModel(description = "not an ignored comment")
data class LocationDTO(

    var id: Long? = null,

    var streetAddress: String? = null,

    var postalCode: String? = null,

    var city: String? = null,

    var stateProvince: String? = null,

    var countryId: Long? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LocationDTO) return false
        val locationDTO = other as LocationDTO
        if (locationDTO.id == null || id == null) {
            return false
        }
        return id == locationDTO.id
    }

    override fun hashCode() = id.hashCode()
}
