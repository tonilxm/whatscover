package com.whatscover.repository.search;

import com.whatscover.domain.InsuranceAgency;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the InsuranceAgency entity.
 */
public interface InsuranceAgencySearchRepository extends ElasticsearchRepository<InsuranceAgency, Long> {
}
