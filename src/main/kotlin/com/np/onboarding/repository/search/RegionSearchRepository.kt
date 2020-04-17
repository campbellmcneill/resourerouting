package com.np.onboarding.repository.search

import com.np.onboarding.domain.Region
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * Spring Data Elasticsearch repository for the [Region] entity.
 */
interface RegionSearchRepository : ElasticsearchRepository<Region, Long>
