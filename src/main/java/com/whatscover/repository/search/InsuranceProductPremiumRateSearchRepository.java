package com.whatscover.repository.search;

import com.whatscover.domain.InsuranceProductPremiumRate;
import com.whatscover.repository.search.ext.InsuranceProductPremiumRateSearchExtRepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the InsuranceProductPremiumRate entity.
 */
public interface InsuranceProductPremiumRateSearchRepository extends ElasticsearchRepository<InsuranceProductPremiumRate, Long>, InsuranceProductPremiumRateSearchExtRepository {
}
