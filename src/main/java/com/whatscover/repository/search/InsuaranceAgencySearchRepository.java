package com.whatscover.repository.search;

import com.whatscover.domain.InsuaranceAgency;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the InsuaranceAgency entity.
 */
public interface InsuaranceAgencySearchRepository extends ElasticsearchRepository<InsuaranceAgency, Long> {
}
