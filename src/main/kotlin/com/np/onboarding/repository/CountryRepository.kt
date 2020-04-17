package com.np.onboarding.repository

import com.np.onboarding.domain.Country
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [Country] entity.
 */
@Suppress("unused")
@Repository
interface CountryRepository : JpaRepository<Country, Long>
