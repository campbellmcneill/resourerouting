package com.np.onboarding.repository.search

import com.np.onboarding.domain.Task
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * Spring Data Elasticsearch repository for the [Task] entity.
 */
interface TaskSearchRepository : ElasticsearchRepository<Task, Long>
