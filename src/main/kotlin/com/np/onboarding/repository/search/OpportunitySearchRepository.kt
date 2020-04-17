package com.np.onboarding.repository.search

import com.np.onboarding.domain.Opportunity
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * Spring Data Elasticsearch repository for the [Opportunity] entity.
 */
interface OpportunitySearchRepository : ElasticsearchRepository<Opportunity, Long>
