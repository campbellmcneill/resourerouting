package com.np.onboarding.repository

import com.np.onboarding.domain.Location
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [Location] entity.
 */
@Suppress("unused")
@Repository
interface LocationRepository : JpaRepository<Location, Long>
