package com.whatscover.repository.search;

import com.whatscover.domain.ProductCategory;
import com.whatscover.repository.search.ext.ProductCategorySearchExtRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProductCategory entity.
 */
public interface ProductCategorySearchRepository extends ElasticsearchRepository<ProductCategory, Long>
    , ProductCategorySearchExtRepository<ProductCategory, Long> {
}
