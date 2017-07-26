package com.whatscover.web.rest;

import com.whatscover.WhatscoverApp;

import com.whatscover.domain.ProductCategory;
import com.whatscover.repository.ProductCategoryRepository;
import com.whatscover.service.ProductCategoryService;
import com.whatscover.repository.search.ProductCategorySearchRepository;
import com.whatscover.service.dto.ProductCategoryDTO;
import com.whatscover.service.mapper.ProductCategoryMapper;
import com.whatscover.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProductCategoryResource REST controller.
 *
 * @see ProductCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WhatscoverApp.class)
public class ProductCategoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductCategorySearchRepository productCategorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProductCategoryMockMvc;

    private ProductCategory productCategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProductCategoryResource productCategoryResource = new ProductCategoryResource(productCategoryService);
        this.restProductCategoryMockMvc = MockMvcBuilders.standaloneSetup(productCategoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductCategory createEntity(EntityManager em) {
        ProductCategory productCategory = new ProductCategory()
            .name(DEFAULT_NAME);
        return productCategory;
    }

    @Before
    public void initTest() {
        productCategorySearchRepository.deleteAll();
        productCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductCategory() throws Exception {
        int databaseSizeBeforeCreate = productCategoryRepository.findAll().size();

        // Create the ProductCategory
        ProductCategoryDTO productCategoryDTO = productCategoryMapper.toDto(productCategory);
        restProductCategoryMockMvc.perform(post("/api/product-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productCategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ProductCategory testProductCategory = productCategoryList.get(productCategoryList.size() - 1);
        assertThat(testProductCategory.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the ProductCategory in Elasticsearch
        ProductCategory productCategoryEs = productCategorySearchRepository.findOne(testProductCategory.getId());
        assertThat(productCategoryEs).isEqualToComparingFieldByField(testProductCategory);
    }

    @Test
    @Transactional
    public void createProductCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productCategoryRepository.findAll().size();

        // Create the ProductCategory with an existing ID
        productCategory.setId(1L);
        ProductCategoryDTO productCategoryDTO = productCategoryMapper.toDto(productCategory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductCategoryMockMvc.perform(post("/api/product-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProductCategories() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList
        restProductCategoryMockMvc.perform(get("/api/product-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getProductCategory() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get the productCategory
        restProductCategoryMockMvc.perform(get("/api/product-categories/{id}", productCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProductCategory() throws Exception {
        // Get the productCategory
        restProductCategoryMockMvc.perform(get("/api/product-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductCategory() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);
        productCategorySearchRepository.save(productCategory);
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();

        // Update the productCategory
        ProductCategory updatedProductCategory = productCategoryRepository.findOne(productCategory.getId());
        updatedProductCategory
            .name(UPDATED_NAME);
        ProductCategoryDTO productCategoryDTO = productCategoryMapper.toDto(updatedProductCategory);

        restProductCategoryMockMvc.perform(put("/api/product-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productCategoryDTO)))
            .andExpect(status().isOk());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
        ProductCategory testProductCategory = productCategoryList.get(productCategoryList.size() - 1);
        assertThat(testProductCategory.getName()).isEqualTo(UPDATED_NAME);

        // Validate the ProductCategory in Elasticsearch
        ProductCategory productCategoryEs = productCategorySearchRepository.findOne(testProductCategory.getId());
        assertThat(productCategoryEs).isEqualToComparingFieldByField(testProductCategory);
    }

    @Test
    @Transactional
    public void updateNonExistingProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();

        // Create the ProductCategory
        ProductCategoryDTO productCategoryDTO = productCategoryMapper.toDto(productCategory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProductCategoryMockMvc.perform(put("/api/product-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productCategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProductCategory() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);
        productCategorySearchRepository.save(productCategory);
        int databaseSizeBeforeDelete = productCategoryRepository.findAll().size();

        // Get the productCategory
        restProductCategoryMockMvc.perform(delete("/api/product-categories/{id}", productCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean productCategoryExistsInEs = productCategorySearchRepository.exists(productCategory.getId());
        assertThat(productCategoryExistsInEs).isFalse();

        // Validate the database is empty
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProductCategory() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);
        productCategorySearchRepository.save(productCategory);

        // Search the productCategory
        restProductCategoryMockMvc.perform(get("/api/_search/product-categories?query=id:" + productCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductCategory.class);
        ProductCategory productCategory1 = new ProductCategory();
        productCategory1.setId(1L);
        ProductCategory productCategory2 = new ProductCategory();
        productCategory2.setId(productCategory1.getId());
        assertThat(productCategory1).isEqualTo(productCategory2);
        productCategory2.setId(2L);
        assertThat(productCategory1).isNotEqualTo(productCategory2);
        productCategory1.setId(null);
        assertThat(productCategory1).isNotEqualTo(productCategory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductCategoryDTO.class);
        ProductCategoryDTO productCategoryDTO1 = new ProductCategoryDTO();
        productCategoryDTO1.setId(1L);
        ProductCategoryDTO productCategoryDTO2 = new ProductCategoryDTO();
        assertThat(productCategoryDTO1).isNotEqualTo(productCategoryDTO2);
        productCategoryDTO2.setId(productCategoryDTO1.getId());
        assertThat(productCategoryDTO1).isEqualTo(productCategoryDTO2);
        productCategoryDTO2.setId(2L);
        assertThat(productCategoryDTO1).isNotEqualTo(productCategoryDTO2);
        productCategoryDTO1.setId(null);
        assertThat(productCategoryDTO1).isNotEqualTo(productCategoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(productCategoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(productCategoryMapper.fromId(null)).isNull();
    }
}
