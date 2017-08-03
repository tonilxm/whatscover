package com.whatscover.repository.search.ext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by toni on 28/07/2017.
 */
public interface ProductCategorySearchExtRepository<ProductCategory, Long> {
    Page<ProductCategory> searchByName(String name, Pageable pageable);
}
