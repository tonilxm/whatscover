package com.whatscover.repository.search;

import com.whatscover.domain.AgentProfile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AgentProfile entity.
 */
public interface AgentProfileSearchRepository extends ElasticsearchRepository<AgentProfile, Long> {
}
