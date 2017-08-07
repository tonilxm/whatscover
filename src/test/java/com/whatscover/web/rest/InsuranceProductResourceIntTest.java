package com.whatscover.web.rest;

import com.whatscover.WhatscoverApp;

import com.whatscover.domain.InsuranceProduct;
import com.whatscover.domain.ProductCategory;
import com.whatscover.domain.InsuranceCompany;
import com.whatscover.repository.InsuranceProductRepository;
import com.whatscover.service.InsuranceProductPremiumRateService;
import com.whatscover.service.InsuranceProductService;
import com.whatscover.repository.search.InsuranceProductSearchRepository;
import com.whatscover.service.dto.InsuranceProductDTO;
import com.whatscover.service.mapper.InsuranceProductMapper;
import com.whatscover.web.rest.errors.ExceptionTranslator;
import com.whatscover.web.rest.util.JsonConverter;

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

import com.whatscover.domain.enumeration.Gender;
/**
 * Test class for the InsuranceProductResource REST controller.
 *
 * @see InsuranceProductResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WhatscoverApp.class)
public class InsuranceProductResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final Double DEFAULT_PRODUCT_WEIGHT_PA = 1D;
    private static final Double UPDATED_PRODUCT_WEIGHT_PA = 2D;

    private static final Double DEFAULT_PRODUCT_WEIGHT_HOSP_INCOME = 1D;
    private static final Double UPDATED_PRODUCT_WEIGHT_HOSP_INCOME = 2D;

    private static final Double DEFAULT_PRODUCT_WEIGHT_CI = 1D;
    private static final Double UPDATED_PRODUCT_WEIGHT_CI = 2D;

    private static final Double DEFAULT_PRODUCT_WEIGHT_CANCER = 1D;
    private static final Double UPDATED_PRODUCT_WEIGHT_CANCER = 2D;
    
    private static final String DEFAULT_SHORT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LONG_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LONG_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_MIN_ENTRY_AGE_LAST_BDAY = 1;
    private static final Integer UPDATED_MIN_ENTRY_AGE_LAST_BDAY = 2;

    private static final Integer DEFAULT_MAX_ENTRY_AGE_LAST_BDAY = 1;
    private static final Integer UPDATED_MAX_ENTRY_AGE_LAST_BDAY = 2;

    private static final Double DEFAULT_MIN_SUM_ASSURED = 1D;
    private static final Double UPDATED_MIN_SUM_ASSURED = 2D;

    private static final Double DEFAULT_MAX_SUM_ASSURED = 1D;
    private static final Double UPDATED_MAX_SUM_ASSURED = 2D;

    private static final Double DEFAULT_PREM_UNIT = 1D;
    private static final Double UPDATED_PREM_UNIT = 2D;

    private static final Double DEFAULT_PROD_WEIGHT_LIFE = 1D;
    private static final Double UPDATED_PROD_WEIGHT_LIFE = 2D;

    private static final Double DEFAULT_PROD_WEIGHT_MEDICAL = 1D;
    private static final Double UPDATED_PROD_WEIGHT_MEDICAL = 2D;

    @Autowired
    private InsuranceProductRepository insuranceProductRepository;

    @Autowired
    private InsuranceProductMapper insuranceProductMapper;

    @Autowired
    private InsuranceProductService insuranceProductService;

    @Autowired
    private InsuranceProductSearchRepository insuranceProductSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInsuranceProductMockMvc;

    private InsuranceProduct insuranceProduct;
    
    @Autowired
    private JsonConverter converter;
    
    @Autowired
    private InsuranceProductPremiumRateService insuranceProductPremiumRateService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InsuranceProductResource insuranceProductResource = new InsuranceProductResource(insuranceProductService, insuranceProductPremiumRateService, converter);
        this.restInsuranceProductMockMvc = MockMvcBuilders.standaloneSetup(insuranceProductResource)
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
    public static InsuranceProduct createEntity(EntityManager em) {
        InsuranceProduct insuranceProduct = new InsuranceProduct()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .gender(DEFAULT_GENDER)
            .productWeightPA(DEFAULT_PRODUCT_WEIGHT_PA)
            .productWeightHospIncome(DEFAULT_PRODUCT_WEIGHT_HOSP_INCOME)
            .productWeightCI(DEFAULT_PRODUCT_WEIGHT_CI)
            .productWeightCancer(DEFAULT_PRODUCT_WEIGHT_CANCER)
            .shortDescription(DEFAULT_SHORT_DESCRIPTION)
            .longDescription(DEFAULT_LONG_DESCRIPTION)
            .minEntryAgeLastBday(DEFAULT_MIN_ENTRY_AGE_LAST_BDAY)
            .maxEntryAgeLastBday(DEFAULT_MAX_ENTRY_AGE_LAST_BDAY)
            .minSumAssured(DEFAULT_MIN_SUM_ASSURED)
            .maxSumAssured(DEFAULT_MAX_SUM_ASSURED)
            .premUnit(DEFAULT_PREM_UNIT)
            .prodWeightLife(DEFAULT_PROD_WEIGHT_LIFE)
            .prodWeightMedical(DEFAULT_PROD_WEIGHT_MEDICAL);
        // Add required entity
        InsuranceCompany insuranceCompany = InsuranceCompanyResourceIntTest.createEntity(em);
        em.persist(insuranceCompany);
        em.flush();
        insuranceProduct.setInsuranceCompany(insuranceCompany);
        // Add required entity
        ProductCategory productCategory = ProductCategoryResourceIntTest.createEntity(em);
        em.persist(productCategory);
        em.flush();
        insuranceProduct.setProductCategory(productCategory);
        return insuranceProduct;
    }

    @Before
    public void initTest() {
        insuranceProductSearchRepository.deleteAll();
        insuranceProduct = createEntity(em);
    }

    @Test
    @Transactional
    public void createInsuranceProduct() throws Exception {
        int databaseSizeBeforeCreate = insuranceProductRepository.findAll().size();

        // Create the InsuranceProduct
        InsuranceProductDTO insuranceProductDTO = insuranceProductMapper.toDto(insuranceProduct);
        restInsuranceProductMockMvc.perform(post("/api/insurance-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceProductDTO)))
            .andExpect(status().isCreated());

        // Validate the InsuranceProduct in the database
        List<InsuranceProduct> insuranceProductList = insuranceProductRepository.findAll();
        assertThat(insuranceProductList).hasSize(databaseSizeBeforeCreate + 1);
        InsuranceProduct testInsuranceProduct = insuranceProductList.get(insuranceProductList.size() - 1);
        assertThat(testInsuranceProduct.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testInsuranceProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInsuranceProduct.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testInsuranceProduct.getProductWeightPA()).isEqualTo(DEFAULT_PRODUCT_WEIGHT_PA);
        assertThat(testInsuranceProduct.getProductWeightHospIncome()).isEqualTo(DEFAULT_PRODUCT_WEIGHT_HOSP_INCOME);
        assertThat(testInsuranceProduct.getProductWeightCI()).isEqualTo(DEFAULT_PRODUCT_WEIGHT_CI);
        assertThat(testInsuranceProduct.getProductWeightCancer()).isEqualTo(DEFAULT_PRODUCT_WEIGHT_CANCER);
        assertThat(testInsuranceProduct.getShortDescription()).isEqualTo(DEFAULT_SHORT_DESCRIPTION);
        assertThat(testInsuranceProduct.getLongDescription()).isEqualTo(DEFAULT_LONG_DESCRIPTION);
        assertThat(testInsuranceProduct.getMinEntryAgeLastBday()).isEqualTo(DEFAULT_MIN_ENTRY_AGE_LAST_BDAY);
        assertThat(testInsuranceProduct.getMaxEntryAgeLastBday()).isEqualTo(DEFAULT_MAX_ENTRY_AGE_LAST_BDAY);
        assertThat(testInsuranceProduct.getMinSumAssured()).isEqualTo(DEFAULT_MIN_SUM_ASSURED);
        assertThat(testInsuranceProduct.getMaxSumAssured()).isEqualTo(DEFAULT_MAX_SUM_ASSURED);
        assertThat(testInsuranceProduct.getPremUnit()).isEqualTo(DEFAULT_PREM_UNIT);
        assertThat(testInsuranceProduct.getProdWeightLife()).isEqualTo(DEFAULT_PROD_WEIGHT_LIFE);
        assertThat(testInsuranceProduct.getProdWeightMedical()).isEqualTo(DEFAULT_PROD_WEIGHT_MEDICAL);

        // Validate the InsuranceProduct in Elasticsearch
        InsuranceProduct insuranceProductEs = insuranceProductSearchRepository.findOne(testInsuranceProduct.getId());
        assertThat(insuranceProductEs).isEqualToComparingFieldByField(testInsuranceProduct);
    }

    @Test
    @Transactional
    public void createInsuranceProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = insuranceProductRepository.findAll().size();

        // Create the InsuranceProduct with an existing ID
        insuranceProduct.setId(1L);
        InsuranceProductDTO insuranceProductDTO = insuranceProductMapper.toDto(insuranceProduct);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceProductMockMvc.perform(post("/api/insurance-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceProductDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<InsuranceProduct> insuranceProductList = insuranceProductRepository.findAll();
        assertThat(insuranceProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceProductRepository.findAll().size();
        // set the field null
        insuranceProduct.setCode(null);

        // Create the InsuranceProduct, which fails.
        InsuranceProductDTO insuranceProductDTO = insuranceProductMapper.toDto(insuranceProduct);

        restInsuranceProductMockMvc.perform(post("/api/insurance-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceProductDTO)))
            .andExpect(status().isBadRequest());

        List<InsuranceProduct> insuranceProductList = insuranceProductRepository.findAll();
        assertThat(insuranceProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceProductRepository.findAll().size();
        // set the field null
        insuranceProduct.setName(null);

        // Create the InsuranceProduct, which fails.
        InsuranceProductDTO insuranceProductDTO = insuranceProductMapper.toDto(insuranceProduct);

        restInsuranceProductMockMvc.perform(post("/api/insurance-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceProductDTO)))
            .andExpect(status().isBadRequest());

        List<InsuranceProduct> insuranceProductList = insuranceProductRepository.findAll();
        assertThat(insuranceProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInsuranceProducts() throws Exception {
        // Initialize the database
        insuranceProductRepository.saveAndFlush(insuranceProduct);

        // Get all the insuranceProductList
        restInsuranceProductMockMvc.perform(get("/api/insurance-products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].productWeightPA").value(hasItem(DEFAULT_PRODUCT_WEIGHT_PA.doubleValue())))
            .andExpect(jsonPath("$.[*].productWeightHospIncome").value(hasItem(DEFAULT_PRODUCT_WEIGHT_HOSP_INCOME.doubleValue())))
            .andExpect(jsonPath("$.[*].productWeightCI").value(hasItem(DEFAULT_PRODUCT_WEIGHT_CI.doubleValue())))
            .andExpect(jsonPath("$.[*].productWeightCancer").value(hasItem(DEFAULT_PRODUCT_WEIGHT_CANCER.doubleValue())))
            .andExpect(jsonPath("$.[*].shortDescription").value(hasItem(DEFAULT_SHORT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].minEntryAgeLastBday").value(hasItem(DEFAULT_MIN_ENTRY_AGE_LAST_BDAY)))
            .andExpect(jsonPath("$.[*].maxEntryAgeLastBday").value(hasItem(DEFAULT_MAX_ENTRY_AGE_LAST_BDAY)))
            .andExpect(jsonPath("$.[*].minSumAssured").value(hasItem(DEFAULT_MIN_SUM_ASSURED.doubleValue())))
            .andExpect(jsonPath("$.[*].maxSumAssured").value(hasItem(DEFAULT_MAX_SUM_ASSURED.doubleValue())))
            .andExpect(jsonPath("$.[*].premUnit").value(hasItem(DEFAULT_PREM_UNIT.doubleValue())))
            .andExpect(jsonPath("$.[*].prodWeightLife").value(hasItem(DEFAULT_PROD_WEIGHT_LIFE.doubleValue())))
            .andExpect(jsonPath("$.[*].prodWeightMedical").value(hasItem(DEFAULT_PROD_WEIGHT_MEDICAL.doubleValue())));
    }

    @Test
    @Transactional
    public void getInsuranceProduct() throws Exception {
        // Initialize the database
        insuranceProductRepository.saveAndFlush(insuranceProduct);

        // Get the insuranceProduct
        restInsuranceProductMockMvc.perform(get("/api/insurance-products/{id}", insuranceProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(insuranceProduct.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.productWeightPA").value(DEFAULT_PRODUCT_WEIGHT_PA.doubleValue()))
            .andExpect(jsonPath("$.productWeightHospIncome").value(DEFAULT_PRODUCT_WEIGHT_HOSP_INCOME.doubleValue()))
            .andExpect(jsonPath("$.productWeightCI").value(DEFAULT_PRODUCT_WEIGHT_CI.doubleValue()))
            .andExpect(jsonPath("$.productWeightCancer").value(DEFAULT_PRODUCT_WEIGHT_CANCER.doubleValue()))
            .andExpect(jsonPath("$.shortDescription").value(DEFAULT_SHORT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.longDescription").value(DEFAULT_LONG_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.minEntryAgeLastBday").value(DEFAULT_MIN_ENTRY_AGE_LAST_BDAY))
            .andExpect(jsonPath("$.maxEntryAgeLastBday").value(DEFAULT_MAX_ENTRY_AGE_LAST_BDAY))
            .andExpect(jsonPath("$.minSumAssured").value(DEFAULT_MIN_SUM_ASSURED.doubleValue()))
            .andExpect(jsonPath("$.maxSumAssured").value(DEFAULT_MAX_SUM_ASSURED.doubleValue()))
            .andExpect(jsonPath("$.premUnit").value(DEFAULT_PREM_UNIT.doubleValue()))
            .andExpect(jsonPath("$.prodWeightLife").value(DEFAULT_PROD_WEIGHT_LIFE.doubleValue()))
            .andExpect(jsonPath("$.prodWeightMedical").value(DEFAULT_PROD_WEIGHT_MEDICAL.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInsuranceProduct() throws Exception {
        // Get the insuranceProduct
        restInsuranceProductMockMvc.perform(get("/api/insurance-products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInsuranceProduct() throws Exception {
        // Initialize the database
        insuranceProductRepository.saveAndFlush(insuranceProduct);
        insuranceProductSearchRepository.save(insuranceProduct);
        int databaseSizeBeforeUpdate = insuranceProductRepository.findAll().size();

        // Update the insuranceProduct
        InsuranceProduct updatedInsuranceProduct = insuranceProductRepository.findOne(insuranceProduct.getId());
        updatedInsuranceProduct
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .productWeightPA(UPDATED_PRODUCT_WEIGHT_PA)
            .productWeightHospIncome(UPDATED_PRODUCT_WEIGHT_HOSP_INCOME)
            .productWeightCI(UPDATED_PRODUCT_WEIGHT_CI)
            .productWeightCancer(UPDATED_PRODUCT_WEIGHT_CANCER)
            .shortDescription(UPDATED_SHORT_DESCRIPTION)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .minEntryAgeLastBday(UPDATED_MIN_ENTRY_AGE_LAST_BDAY)
            .maxEntryAgeLastBday(UPDATED_MAX_ENTRY_AGE_LAST_BDAY)
            .minSumAssured(UPDATED_MIN_SUM_ASSURED)
            .maxSumAssured(UPDATED_MAX_SUM_ASSURED)
            .premUnit(UPDATED_PREM_UNIT)
            .prodWeightLife(UPDATED_PROD_WEIGHT_LIFE)
            .prodWeightMedical(UPDATED_PROD_WEIGHT_MEDICAL);
        InsuranceProductDTO insuranceProductDTO = insuranceProductMapper.toDto(updatedInsuranceProduct);

        restInsuranceProductMockMvc.perform(put("/api/insurance-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceProductDTO)))
            .andExpect(status().isOk());

        // Validate the InsuranceProduct in the database
        List<InsuranceProduct> insuranceProductList = insuranceProductRepository.findAll();
        assertThat(insuranceProductList).hasSize(databaseSizeBeforeUpdate);
        InsuranceProduct testInsuranceProduct = insuranceProductList.get(insuranceProductList.size() - 1);
        assertThat(testInsuranceProduct.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testInsuranceProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInsuranceProduct.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testInsuranceProduct.getProductWeightPA()).isEqualTo(UPDATED_PRODUCT_WEIGHT_PA);
        assertThat(testInsuranceProduct.getProductWeightHospIncome()).isEqualTo(UPDATED_PRODUCT_WEIGHT_HOSP_INCOME);
        assertThat(testInsuranceProduct.getProductWeightCI()).isEqualTo(UPDATED_PRODUCT_WEIGHT_CI);
        assertThat(testInsuranceProduct.getProductWeightCancer()).isEqualTo(UPDATED_PRODUCT_WEIGHT_CANCER);
        assertThat(testInsuranceProduct.getShortDescription()).isEqualTo(UPDATED_SHORT_DESCRIPTION);
        assertThat(testInsuranceProduct.getLongDescription()).isEqualTo(UPDATED_LONG_DESCRIPTION);
        assertThat(testInsuranceProduct.getMinEntryAgeLastBday()).isEqualTo(UPDATED_MIN_ENTRY_AGE_LAST_BDAY);
        assertThat(testInsuranceProduct.getMaxEntryAgeLastBday()).isEqualTo(UPDATED_MAX_ENTRY_AGE_LAST_BDAY);
        assertThat(testInsuranceProduct.getMinSumAssured()).isEqualTo(UPDATED_MIN_SUM_ASSURED);
        assertThat(testInsuranceProduct.getMaxSumAssured()).isEqualTo(UPDATED_MAX_SUM_ASSURED);
        assertThat(testInsuranceProduct.getPremUnit()).isEqualTo(UPDATED_PREM_UNIT);
        assertThat(testInsuranceProduct.getProdWeightLife()).isEqualTo(UPDATED_PROD_WEIGHT_LIFE);
        assertThat(testInsuranceProduct.getProdWeightMedical()).isEqualTo(UPDATED_PROD_WEIGHT_MEDICAL);
        
        // Validate the InsuranceProduct in Elasticsearch
        InsuranceProduct insuranceProductEs = insuranceProductSearchRepository.findOne(testInsuranceProduct.getId());
        assertThat(insuranceProductEs).isEqualToComparingFieldByField(testInsuranceProduct);
    }

    @Test
    @Transactional
    public void updateNonExistingInsuranceProduct() throws Exception {
        int databaseSizeBeforeUpdate = insuranceProductRepository.findAll().size();

        // Create the InsuranceProduct
        InsuranceProductDTO insuranceProductDTO = insuranceProductMapper.toDto(insuranceProduct);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInsuranceProductMockMvc.perform(put("/api/insurance-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceProductDTO)))
            .andExpect(status().isCreated());

        // Validate the InsuranceProduct in the database
        List<InsuranceProduct> insuranceProductList = insuranceProductRepository.findAll();
        assertThat(insuranceProductList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInsuranceProduct() throws Exception {
        // Initialize the database
        insuranceProductRepository.saveAndFlush(insuranceProduct);
        insuranceProductSearchRepository.save(insuranceProduct);
        int databaseSizeBeforeDelete = insuranceProductRepository.findAll().size();

        // Get the insuranceProduct
        restInsuranceProductMockMvc.perform(delete("/api/insurance-products/{id}", insuranceProduct.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean insuranceProductExistsInEs = insuranceProductSearchRepository.exists(insuranceProduct.getId());
        assertThat(insuranceProductExistsInEs).isFalse();

        // Validate the database is empty
        List<InsuranceProduct> insuranceProductList = insuranceProductRepository.findAll();
        assertThat(insuranceProductList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInsuranceProduct() throws Exception {
        // Initialize the database
        insuranceProductRepository.saveAndFlush(insuranceProduct);
        insuranceProductSearchRepository.save(insuranceProduct);

        // Search the insuranceProduct
        restInsuranceProductMockMvc.perform(get("/api/_search/insurance-products?query=id:" + insuranceProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].productWeightPA").value(hasItem(DEFAULT_PRODUCT_WEIGHT_PA.doubleValue())))
            .andExpect(jsonPath("$.[*].productWeightHospIncome").value(hasItem(DEFAULT_PRODUCT_WEIGHT_HOSP_INCOME.doubleValue())))
            .andExpect(jsonPath("$.[*].productWeightCI").value(hasItem(DEFAULT_PRODUCT_WEIGHT_CI.doubleValue())))
            .andExpect(jsonPath("$.[*].productWeightCancer").value(hasItem(DEFAULT_PRODUCT_WEIGHT_CANCER.doubleValue())))
            .andExpect(jsonPath("$.[*].shortDescription").value(hasItem(DEFAULT_SHORT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].minEntryAgeLastBday").value(hasItem(DEFAULT_MIN_ENTRY_AGE_LAST_BDAY)))
            .andExpect(jsonPath("$.[*].maxEntryAgeLastBday").value(hasItem(DEFAULT_MAX_ENTRY_AGE_LAST_BDAY)))
            .andExpect(jsonPath("$.[*].minSumAssured").value(hasItem(DEFAULT_MIN_SUM_ASSURED.doubleValue())))
            .andExpect(jsonPath("$.[*].maxSumAssured").value(hasItem(DEFAULT_MAX_SUM_ASSURED.doubleValue())))
            .andExpect(jsonPath("$.[*].premUnit").value(hasItem(DEFAULT_PREM_UNIT.doubleValue())))
            .andExpect(jsonPath("$.[*].prodWeightLife").value(hasItem(DEFAULT_PROD_WEIGHT_LIFE.doubleValue())))
            .andExpect(jsonPath("$.[*].prodWeightMedical").value(hasItem(DEFAULT_PROD_WEIGHT_MEDICAL.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceProduct.class);
        InsuranceProduct insuranceProduct1 = new InsuranceProduct();
        insuranceProduct1.setId(1L);
        InsuranceProduct insuranceProduct2 = new InsuranceProduct();
        insuranceProduct2.setId(insuranceProduct1.getId());
        assertThat(insuranceProduct1).isEqualTo(insuranceProduct2);
        insuranceProduct2.setId(2L);
        assertThat(insuranceProduct1).isNotEqualTo(insuranceProduct2);
        insuranceProduct1.setId(null);
        assertThat(insuranceProduct1).isNotEqualTo(insuranceProduct2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceProductDTO.class);
        InsuranceProductDTO insuranceProductDTO1 = new InsuranceProductDTO();
        insuranceProductDTO1.setId(1L);
        InsuranceProductDTO insuranceProductDTO2 = new InsuranceProductDTO();
        assertThat(insuranceProductDTO1).isNotEqualTo(insuranceProductDTO2);
        insuranceProductDTO2.setId(insuranceProductDTO1.getId());
        assertThat(insuranceProductDTO1).isEqualTo(insuranceProductDTO2);
        insuranceProductDTO2.setId(2L);
        assertThat(insuranceProductDTO1).isNotEqualTo(insuranceProductDTO2);
        insuranceProductDTO1.setId(null);
        assertThat(insuranceProductDTO1).isNotEqualTo(insuranceProductDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(insuranceProductMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(insuranceProductMapper.fromId(null)).isNull();
    }
}
