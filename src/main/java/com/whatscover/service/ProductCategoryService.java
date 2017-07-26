package com.whatscover.service;

import com.whatscover.service.dto.ProductCategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ProductCategory.
 */
public interface ProductCategoryService {

    /**
     * Save a productCategory.
     *
     * @param productCategoryDTO the entity to save
     * @return the persisted entity
     */
    ProductCategoryDTO save(ProductCategoryDTO productCategoryDTO);

    /**
     *  Get all the productCategories.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ProductCategoryDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" productCategory.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProductCategoryDTO findOne(Long id);

    /**
     *  Delete the "id" productCategory.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the productCategory corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ProductCategoryDTO> search(String query, Pageable pageable);
}
