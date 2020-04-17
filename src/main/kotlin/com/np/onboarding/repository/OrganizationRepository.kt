package com.np.onboarding.repository

import com.np.onboarding.domain.Organization
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [Organization] entity.
 */
@Suppress("unused")
@Repository
interface OrganizationRepository : JpaRepository<Organization, Long>
