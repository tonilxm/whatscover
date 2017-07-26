package com.whatscover.service.impl;

import com.whatscover.service.ProductCategoryService;
import com.whatscover.domain.ProductCategory;
import com.whatscover.repository.ProductCategoryRepository;
import com.whatscover.repository.search.ProductCategorySearchRepository;
import com.whatscover.service.dto.ProductCategoryDTO;
import com.whatscover.service.mapper.ProductCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ProductCategory.
 */
@Service
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService{

    private final Logger log = LoggerFactory.getLogger(ProductCategoryServiceImpl.class);

    private final ProductCategoryRepository productCategoryRepository;

    private final ProductCategoryMapper productCategoryMapper;

    private final ProductCategorySearchRepository productCategorySearchRepository;

    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository, ProductCategoryMapper productCategoryMapper, ProductCategorySearchRepository productCategorySearchRepository) {
        this.productCategoryRepository = productCategoryRepository;
        this.productCategoryMapper = productCategoryMapper;
        this.productCategorySearchRepository = productCategorySearchRepository;
    }

    /**
     * Save a productCategory.
     *
     * @param productCategoryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProductCategoryDTO save(ProductCategoryDTO productCategoryDTO) {
        log.debug("Request to save ProductCategory : {}", productCategoryDTO);
        ProductCategory productCategory = productCategoryMapper.toEntity(productCategoryDTO);
        productCategory = productCategoryRepository.save(productCategory);
        ProductCategoryDTO result = productCategoryMapper.toDto(productCategory);
        productCategorySearchRepository.save(productCategory);
        return result;
    }

    /**
     *  Get all the productCategories.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProductCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductCategories");
        return productCategoryRepository.findAll(pageable)
            .map(productCategoryMapper::toDto);
    }

    /**
     *  Get one productCategory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProductCategoryDTO findOne(Long id) {
        log.debug("Request to get ProductCategory : {}", id);
        ProductCategory productCategory = productCategoryRepository.findOne(id);
        return productCategoryMapper.toDto(productCategory);
    }

    /**
     *  Delete the  productCategory by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductCategory : {}", id);
        productCategoryRepository.delete(id);
        productCategorySearchRepository.delete(id);
    }

    /**
     * Search for the productCategory corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProductCategoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProductCategories for query {}", query);
        Page<ProductCategory> result = productCategorySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(productCategoryMapper::toDto);
    }
}
