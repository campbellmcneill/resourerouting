package com.np.onboarding.repository.search

import com.np.onboarding.domain.Organization
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * Spring Data Elasticsearch repository for the [Organization] entity.
 */
interface OrganizationSearchRepository : ElasticsearchRepository<Organization, Long>
