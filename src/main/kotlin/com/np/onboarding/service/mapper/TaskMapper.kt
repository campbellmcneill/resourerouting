package com.np.onboarding.service.mapper

import com.np.onboarding.domain.Task
import com.np.onboarding.service.dto.TaskDTO
import org.mapstruct.Mapper

/**
 * Mapper for the entity [Task] and its DTO [TaskDTO].
 */
@Mapper(componentModel = "spring", uses = [])
interface TaskMapper :
    EntityMapper<TaskDTO, Task> {

    override fun toEntity(taskDTO: TaskDTO): Task

    @JvmDefault
    fun fromId(id: Long?) = id?.let {
        val task = Task()
        task.id = id
        task
    }
}
