package com.np.onboarding.repository

import com.np.onboarding.domain.Opportunity
import java.util.Optional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [Opportunity] entity.
 */
@Repository
interface OpportunityRepository : JpaRepository<Opportunity, Long> {

    @Query(value = "select distinct opportunity from Opportunity opportunity left join fetch opportunity.tasks",
        countQuery = "select count(distinct opportunity) from Opportunity opportunity")
    fun findAllWithEagerRelationships(pageable: Pageable): Page<Opportunity>

    @Query("select distinct opportunity from Opportunity opportunity left join fetch opportunity.tasks")
    fun findAllWithEagerRelationships(): MutableList<Opportunity>

    @Query("select opportunity from Opportunity opportunity left join fetch opportunity.tasks where opportunity.id =:id")
    fun findOneWithEagerRelationships(@Param("id") id: Long): Optional<Opportunity>
}
