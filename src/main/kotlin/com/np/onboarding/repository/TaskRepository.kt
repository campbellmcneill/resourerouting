package com.np.onboarding.repository

import com.np.onboarding.domain.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [Task] entity.
 */
@Suppress("unused")
@Repository
interface TaskRepository : JpaRepository<Task, Long>
