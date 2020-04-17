package com.np.onboarding.repository.search

import com.np.onboarding.domain.Location
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * Spring Data Elasticsearch repository for the [Location] entity.
 */
interface LocationSearchRepository : ElasticsearchRepository<Location, Long>
