package com.np.onboarding.repository.search

import com.np.onboarding.domain.Country
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * Spring Data Elasticsearch repository for the [Country] entity.
 */
interface CountrySearchRepository : ElasticsearchRepository<Country, Long>
