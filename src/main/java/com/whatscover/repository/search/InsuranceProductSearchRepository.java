package com.whatscover.repository.search;

import com.whatscover.domain.InsuranceProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the InsuranceProduct entity.
 */
public interface InsuranceProductSearchRepository extends ElasticsearchRepository<InsuranceProduct, Long> {
}
