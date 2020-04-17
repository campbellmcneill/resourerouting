package com.np.onboarding.repository.search

import com.np.onboarding.domain.OpportunityHistory
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * Spring Data Elasticsearch repository for the [OpportunityHistory] entity.
 */
interface OpportunityHistorySearchRepository : ElasticsearchRepository<OpportunityHistory, Long>
