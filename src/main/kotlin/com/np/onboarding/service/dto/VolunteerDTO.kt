package com.np.onboarding.service.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.io.Serializable
import java.time.Instant

/**
 * A DTO for the [com.np.onboarding.domain.Volunteer] entity.
 */
@ApiModel(description = "The Employee entity.")
data class VolunteerDTO(

    var id: Long? = null,
    /**
     * The firstname attribute.
     */

    @ApiModelProperty(value = "The firstname attribute.")
    var firstName: String? = null,

    var lastName: String? = null,

    var email: String? = null,

    var phoneNumber: String? = null,

    var hireDate: Instant? = null,

    var supervisorId: Long? = null,
    /**
     * Another side of the same relationship
     */
    @ApiModelProperty(value = "Another side of the same relationship")

    var organizationId: Long? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VolunteerDTO) return false
        val volunteerDTO = other as VolunteerDTO
        if (volunteerDTO.id == null || id == null) {
            return false
        }
        return id == volunteerDTO.id
    }

    override fun hashCode() = id.hashCode()
}
