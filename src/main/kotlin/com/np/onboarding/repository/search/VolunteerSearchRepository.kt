package com.np.onboarding.repository.search

import com.np.onboarding.domain.Volunteer
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * Spring Data Elasticsearch repository for the [Volunteer] entity.
 */
interface VolunteerSearchRepository : ElasticsearchRepository<Volunteer, Long>
