package com.np.onboarding.service.dto

import io.swagger.annotations.ApiModel
import java.io.Serializable

/**
 * A DTO for the [com.np.onboarding.domain.Task] entity.
 */
@ApiModel(description = "Task entity.\n@author The JHipster team.")
data class TaskDTO(

    var id: Long? = null,

    var title: String? = null,

    var description: String? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TaskDTO) return false
        val taskDTO = other as TaskDTO
        if (taskDTO.id == null || id == null) {
            return false
        }
        return id == taskDTO.id
    }

    override fun hashCode() = id.hashCode()
}
