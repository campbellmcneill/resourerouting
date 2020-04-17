package com.np.onboarding.repository.search

import com.np.onboarding.domain.User
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * Spring Data Elasticsearch repository for the User entity.
 */
interface UserSearchRepository : ElasticsearchRepository<User, Long>
