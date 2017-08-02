package com.whatscover.repository.search;

import com.whatscover.domain.InsuranceProductPremiumRate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the InsuranceProductPremiumRate entity.
 */
public interface InsuranceProductPremiumRateSearchRepository extends ElasticsearchRepository<InsuranceProductPremiumRate, Long> {
}
