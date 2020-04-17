package com.np.onboarding.repository

import com.np.onboarding.domain.OpportunityHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [OpportunityHistory] entity.
 */
@Suppress("unused")
@Repository
interface OpportunityHistoryRepository : JpaRepository<OpportunityHistory, Long>
