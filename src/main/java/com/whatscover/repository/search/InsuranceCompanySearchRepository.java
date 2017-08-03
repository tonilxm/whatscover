package com.whatscover.repository.search;

import com.whatscover.domain.InsuranceCompany;
import com.whatscover.repository.search.ext.InsuranceCompanySearchExtRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the InsuranceCompany entity.
 */
public interface InsuranceCompanySearchRepository extends ElasticsearchRepository<InsuranceCompany, Long>
    , InsuranceCompanySearchExtRepository {
}
