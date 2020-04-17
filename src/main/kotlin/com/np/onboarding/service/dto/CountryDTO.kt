package com.np.onboarding.service.dto

import java.io.Serializable

/**
 * A DTO for the [com.np.onboarding.domain.Country] entity.
 */
data class CountryDTO(

    var id: Long? = null,

    var countryName: String? = null,

    var regionId: Long? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CountryDTO) return false
        val countryDTO = other as CountryDTO
        if (countryDTO.id == null || id == null) {
            return false
        }
        return id == countryDTO.id
    }

    override fun hashCode() = id.hashCode()
}
