package com.whatscover.repository.search;

import com.whatscover.domain.CustomerProfile;
import com.whatscover.repository.search.ext.CustomerProfileSearchExtRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CustomerProfile entity.
 */
public interface CustomerProfileSearchRepository extends ElasticsearchRepository<CustomerProfile, Long>, CustomerProfileSearchExtRepository {
}
