package com.np.onboarding.repository

import com.np.onboarding.domain.Volunteer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [Volunteer] entity.
 */
@Suppress("unused")
@Repository
interface VolunteerRepository : JpaRepository<Volunteer, Long>
