package com.whatscover.web.rest;

import com.whatscover.WhatscoverApp;

import com.whatscover.domain.InsuranceProduct;
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

    private static final Integer DEFAULT_ENTRY_AGE_LAST_BDAY = 1;
    private static final Integer UPDATED_ENTRY_AGE_LAST_BDAY = 2;

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final Integer DEFAULT_PREMIUM_TERM = 1;
    private static final Integer UPDATED_PREMIUM_TERM = 2;

    private static final Integer DEFAULT_POLICY_TERM = 1;
    private static final Integer UPDATED_POLICY_TERM = 2;

    private static final Double DEFAULT_PREMIUM_RATE = 1D;
    private static final Double UPDATED_PREMIUM_RATE = 2D;

    private static final Double DEFAULT_SUM_ASSURED_DEATH = 1D;
    private static final Double UPDATED_SUM_ASSURED_DEATH = 2D;

    private static final Double DEFAULT_SUM_ASSURED_TPD = 1D;
    private static final Double UPDATED_SUM_ASSURED_TPD = 2D;

    private static final Double DEFAULT_SUM_ASSURED_ADD = 1D;
    private static final Double UPDATED_SUM_ASSURED_ADD = 2D;

    private static final Double DEFAULT_SUM_ASSURED_HOSP_INCOME = 1D;
    private static final Double UPDATED_SUM_ASSURED_HOSP_INCOME = 2D;

    private static final Double DEFAULT_SUM_ASSURED_CI = 1D;
    private static final Double UPDATED_SUM_ASSURED_CI = 2D;

    private static final Double DEFAULT_SUM_ASSURED_MEDIC = 1D;
    private static final Double UPDATED_SUM_ASSURED_MEDIC = 2D;

    private static final Double DEFAULT_SUM_ASSURED_CANCER = 1D;
    private static final Double UPDATED_SUM_ASSURED_CANCER = 2D;

    private static final Double DEFAULT_PRODUCT_WEIGHT_DEATH = 1D;
    private static final Double UPDATED_PRODUCT_WEIGHT_DEATH = 2D;

    private static final Double DEFAULT_PRODUCT_WEIGHT_PA = 1D;
    private static final Double UPDATED_PRODUCT_WEIGHT_PA = 2D;

    private static final Double DEFAULT_PRODUCT_WEIGHT_HOSP_INCOME = 1D;
    private static final Double UPDATED_PRODUCT_WEIGHT_HOSP_INCOME = 2D;

    private static final Double DEFAULT_PRODUCT_WEIGHT_CI = 1D;
    private static final Double UPDATED_PRODUCT_WEIGHT_CI = 2D;

    private static final Double DEFAULT_PRODUCT_WEIGHT_MEDIC = 1D;
    private static final Double UPDATED_PRODUCT_WEIGHT_MEDIC = 2D;

    private static final Double DEFAULT_PRODUCT_WEIGHT_CANCER = 1D;
    private static final Double UPDATED_PRODUCT_WEIGHT_CANCER = 2D;

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
            .entryAgeLastBday(DEFAULT_ENTRY_AGE_LAST_BDAY)
            .gender(DEFAULT_GENDER)
            .premiumTerm(DEFAULT_PREMIUM_TERM)
            .policyTerm(DEFAULT_POLICY_TERM)
            .premiumRate(DEFAULT_PREMIUM_RATE)
            .sumAssuredDeath(DEFAULT_SUM_ASSURED_DEATH)
            .sumAssuredTPD(DEFAULT_SUM_ASSURED_TPD)
            .sumAssuredADD(DEFAULT_SUM_ASSURED_ADD)
            .sumAssuredHospIncome(DEFAULT_SUM_ASSURED_HOSP_INCOME)
            .sumAssuredCI(DEFAULT_SUM_ASSURED_CI)
            .sumAssuredMedic(DEFAULT_SUM_ASSURED_MEDIC)
            .sumAssuredCancer(DEFAULT_SUM_ASSURED_CANCER)
            .productWeightDeath(DEFAULT_PRODUCT_WEIGHT_DEATH)
            .productWeightPA(DEFAULT_PRODUCT_WEIGHT_PA)
            .productWeightHospIncome(DEFAULT_PRODUCT_WEIGHT_HOSP_INCOME)
            .productWeightCI(DEFAULT_PRODUCT_WEIGHT_CI)
            .productWeightMedic(DEFAULT_PRODUCT_WEIGHT_MEDIC)
            .productWeightCancer(DEFAULT_PRODUCT_WEIGHT_CANCER);
        // Add required entity
        InsuranceCompany insuranceCompany = InsuranceCompanyResourceIntTest.createEntity(em);
        em.persist(insuranceCompany);
        em.flush();
        insuranceProduct.setInsuranceCompany(insuranceCompany);
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
        assertThat(testInsuranceProduct.getEntryAgeLastBday()).isEqualTo(DEFAULT_ENTRY_AGE_LAST_BDAY);
        assertThat(testInsuranceProduct.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testInsuranceProduct.getPremiumTerm()).isEqualTo(DEFAULT_PREMIUM_TERM);
        assertThat(testInsuranceProduct.getPolicyTerm()).isEqualTo(DEFAULT_POLICY_TERM);
        assertThat(testInsuranceProduct.getPremiumRate()).isEqualTo(DEFAULT_PREMIUM_RATE);
        assertThat(testInsuranceProduct.getSumAssuredDeath()).isEqualTo(DEFAULT_SUM_ASSURED_DEATH);
        assertThat(testInsuranceProduct.getSumAssuredTPD()).isEqualTo(DEFAULT_SUM_ASSURED_TPD);
        assertThat(testInsuranceProduct.getSumAssuredADD()).isEqualTo(DEFAULT_SUM_ASSURED_ADD);
        assertThat(testInsuranceProduct.getSumAssuredHospIncome()).isEqualTo(DEFAULT_SUM_ASSURED_HOSP_INCOME);
        assertThat(testInsuranceProduct.getSumAssuredCI()).isEqualTo(DEFAULT_SUM_ASSURED_CI);
        assertThat(testInsuranceProduct.getSumAssuredMedic()).isEqualTo(DEFAULT_SUM_ASSURED_MEDIC);
        assertThat(testInsuranceProduct.getSumAssuredCancer()).isEqualTo(DEFAULT_SUM_ASSURED_CANCER);
        assertThat(testInsuranceProduct.getProductWeightDeath()).isEqualTo(DEFAULT_PRODUCT_WEIGHT_DEATH);
        assertThat(testInsuranceProduct.getProductWeightPA()).isEqualTo(DEFAULT_PRODUCT_WEIGHT_PA);
        assertThat(testInsuranceProduct.getProductWeightHospIncome()).isEqualTo(DEFAULT_PRODUCT_WEIGHT_HOSP_INCOME);
        assertThat(testInsuranceProduct.getProductWeightCI()).isEqualTo(DEFAULT_PRODUCT_WEIGHT_CI);
        assertThat(testInsuranceProduct.getProductWeightMedic()).isEqualTo(DEFAULT_PRODUCT_WEIGHT_MEDIC);
        assertThat(testInsuranceProduct.getProductWeightCancer()).isEqualTo(DEFAULT_PRODUCT_WEIGHT_CANCER);

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
            .andExpect(jsonPath("$.[*].entryAgeLastBday").value(hasItem(DEFAULT_ENTRY_AGE_LAST_BDAY)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].premiumTerm").value(hasItem(DEFAULT_PREMIUM_TERM)))
            .andExpect(jsonPath("$.[*].policyTerm").value(hasItem(DEFAULT_POLICY_TERM)))
            .andExpect(jsonPath("$.[*].premiumRate").value(hasItem(DEFAULT_PREMIUM_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].sumAssuredDeath").value(hasItem(DEFAULT_SUM_ASSURED_DEATH.doubleValue())))
            .andExpect(jsonPath("$.[*].sumAssuredTPD").value(hasItem(DEFAULT_SUM_ASSURED_TPD.doubleValue())))
            .andExpect(jsonPath("$.[*].sumAssuredADD").value(hasItem(DEFAULT_SUM_ASSURED_ADD.doubleValue())))
            .andExpect(jsonPath("$.[*].sumAssuredHospIncome").value(hasItem(DEFAULT_SUM_ASSURED_HOSP_INCOME.doubleValue())))
            .andExpect(jsonPath("$.[*].sumAssuredCI").value(hasItem(DEFAULT_SUM_ASSURED_CI.doubleValue())))
            .andExpect(jsonPath("$.[*].sumAssuredMedic").value(hasItem(DEFAULT_SUM_ASSURED_MEDIC.doubleValue())))
            .andExpect(jsonPath("$.[*].sumAssuredCancer").value(hasItem(DEFAULT_SUM_ASSURED_CANCER.doubleValue())))
            .andExpect(jsonPath("$.[*].productWeightDeath").value(hasItem(DEFAULT_PRODUCT_WEIGHT_DEATH.doubleValue())))
            .andExpect(jsonPath("$.[*].productWeightPA").value(hasItem(DEFAULT_PRODUCT_WEIGHT_PA.doubleValue())))
            .andExpect(jsonPath("$.[*].productWeightHospIncome").value(hasItem(DEFAULT_PRODUCT_WEIGHT_HOSP_INCOME.doubleValue())))
            .andExpect(jsonPath("$.[*].productWeightCI").value(hasItem(DEFAULT_PRODUCT_WEIGHT_CI.doubleValue())))
            .andExpect(jsonPath("$.[*].productWeightMedic").value(hasItem(DEFAULT_PRODUCT_WEIGHT_MEDIC.doubleValue())))
            .andExpect(jsonPath("$.[*].productWeightCancer").value(hasItem(DEFAULT_PRODUCT_WEIGHT_CANCER.doubleValue())));
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
            .andExpect(jsonPath("$.entryAgeLastBday").value(DEFAULT_ENTRY_AGE_LAST_BDAY))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.premiumTerm").value(DEFAULT_PREMIUM_TERM))
            .andExpect(jsonPath("$.policyTerm").value(DEFAULT_POLICY_TERM))
            .andExpect(jsonPath("$.premiumRate").value(DEFAULT_PREMIUM_RATE.doubleValue()))
            .andExpect(jsonPath("$.sumAssuredDeath").value(DEFAULT_SUM_ASSURED_DEATH.doubleValue()))
            .andExpect(jsonPath("$.sumAssuredTPD").value(DEFAULT_SUM_ASSURED_TPD.doubleValue()))
            .andExpect(jsonPath("$.sumAssuredADD").value(DEFAULT_SUM_ASSURED_ADD.doubleValue()))
            .andExpect(jsonPath("$.sumAssuredHospIncome").value(DEFAULT_SUM_ASSURED_HOSP_INCOME.doubleValue()))
            .andExpect(jsonPath("$.sumAssuredCI").value(DEFAULT_SUM_ASSURED_CI.doubleValue()))
            .andExpect(jsonPath("$.sumAssuredMedic").value(DEFAULT_SUM_ASSURED_MEDIC.doubleValue()))
            .andExpect(jsonPath("$.sumAssuredCancer").value(DEFAULT_SUM_ASSURED_CANCER.doubleValue()))
            .andExpect(jsonPath("$.productWeightDeath").value(DEFAULT_PRODUCT_WEIGHT_DEATH.doubleValue()))
            .andExpect(jsonPath("$.productWeightPA").value(DEFAULT_PRODUCT_WEIGHT_PA.doubleValue()))
            .andExpect(jsonPath("$.productWeightHospIncome").value(DEFAULT_PRODUCT_WEIGHT_HOSP_INCOME.doubleValue()))
            .andExpect(jsonPath("$.productWeightCI").value(DEFAULT_PRODUCT_WEIGHT_CI.doubleValue()))
            .andExpect(jsonPath("$.productWeightMedic").value(DEFAULT_PRODUCT_WEIGHT_MEDIC.doubleValue()))
            .andExpect(jsonPath("$.productWeightCancer").value(DEFAULT_PRODUCT_WEIGHT_CANCER.doubleValue()));
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
            .entryAgeLastBday(UPDATED_ENTRY_AGE_LAST_BDAY)
            .gender(UPDATED_GENDER)
            .premiumTerm(UPDATED_PREMIUM_TERM)
            .policyTerm(UPDATED_POLICY_TERM)
            .premiumRate(UPDATED_PREMIUM_RATE)
            .sumAssuredDeath(UPDATED_SUM_ASSURED_DEATH)
            .sumAssuredTPD(UPDATED_SUM_ASSURED_TPD)
            .sumAssuredADD(UPDATED_SUM_ASSURED_ADD)
            .sumAssuredHospIncome(UPDATED_SUM_ASSURED_HOSP_INCOME)
            .sumAssuredCI(UPDATED_SUM_ASSURED_CI)
            .sumAssuredMedic(UPDATED_SUM_ASSURED_MEDIC)
            .sumAssuredCancer(UPDATED_SUM_ASSURED_CANCER)
            .productWeightDeath(UPDATED_PRODUCT_WEIGHT_DEATH)
            .productWeightPA(UPDATED_PRODUCT_WEIGHT_PA)
            .productWeightHospIncome(UPDATED_PRODUCT_WEIGHT_HOSP_INCOME)
            .productWeightCI(UPDATED_PRODUCT_WEIGHT_CI)
            .productWeightMedic(UPDATED_PRODUCT_WEIGHT_MEDIC)
            .productWeightCancer(UPDATED_PRODUCT_WEIGHT_CANCER);
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
        assertThat(testInsuranceProduct.getEntryAgeLastBday()).isEqualTo(UPDATED_ENTRY_AGE_LAST_BDAY);
        assertThat(testInsuranceProduct.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testInsuranceProduct.getPremiumTerm()).isEqualTo(UPDATED_PREMIUM_TERM);
        assertThat(testInsuranceProduct.getPolicyTerm()).isEqualTo(UPDATED_POLICY_TERM);
        assertThat(testInsuranceProduct.getPremiumRate()).isEqualTo(UPDATED_PREMIUM_RATE);
        assertThat(testInsuranceProduct.getSumAssuredDeath()).isEqualTo(UPDATED_SUM_ASSURED_DEATH);
        assertThat(testInsuranceProduct.getSumAssuredTPD()).isEqualTo(UPDATED_SUM_ASSURED_TPD);
        assertThat(testInsuranceProduct.getSumAssuredADD()).isEqualTo(UPDATED_SUM_ASSURED_ADD);
        assertThat(testInsuranceProduct.getSumAssuredHospIncome()).isEqualTo(UPDATED_SUM_ASSURED_HOSP_INCOME);
        assertThat(testInsuranceProduct.getSumAssuredCI()).isEqualTo(UPDATED_SUM_ASSURED_CI);
        assertThat(testInsuranceProduct.getSumAssuredMedic()).isEqualTo(UPDATED_SUM_ASSURED_MEDIC);
        assertThat(testInsuranceProduct.getSumAssuredCancer()).isEqualTo(UPDATED_SUM_ASSURED_CANCER);
        assertThat(testInsuranceProduct.getProductWeightDeath()).isEqualTo(UPDATED_PRODUCT_WEIGHT_DEATH);
        assertThat(testInsuranceProduct.getProductWeightPA()).isEqualTo(UPDATED_PRODUCT_WEIGHT_PA);
        assertThat(testInsuranceProduct.getProductWeightHospIncome()).isEqualTo(UPDATED_PRODUCT_WEIGHT_HOSP_INCOME);
        assertThat(testInsuranceProduct.getProductWeightCI()).isEqualTo(UPDATED_PRODUCT_WEIGHT_CI);
        assertThat(testInsuranceProduct.getProductWeightMedic()).isEqualTo(UPDATED_PRODUCT_WEIGHT_MEDIC);
        assertThat(testInsuranceProduct.getProductWeightCancer()).isEqualTo(UPDATED_PRODUCT_WEIGHT_CANCER);

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
            .andExpect(jsonPath("$.[*].entryAgeLastBday").value(hasItem(DEFAULT_ENTRY_AGE_LAST_BDAY)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].premiumTerm").value(hasItem(DEFAULT_PREMIUM_TERM)))
            .andExpect(jsonPath("$.[*].policyTerm").value(hasItem(DEFAULT_POLICY_TERM)))
            .andExpect(jsonPath("$.[*].premiumRate").value(hasItem(DEFAULT_PREMIUM_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].sumAssuredDeath").value(hasItem(DEFAULT_SUM_ASSURED_DEATH.doubleValue())))
            .andExpect(jsonPath("$.[*].sumAssuredTPD").value(hasItem(DEFAULT_SUM_ASSURED_TPD.doubleValue())))
            .andExpect(jsonPath("$.[*].sumAssuredADD").value(hasItem(DEFAULT_SUM_ASSURED_ADD.doubleValue())))
            .andExpect(jsonPath("$.[*].sumAssuredHospIncome").value(hasItem(DEFAULT_SUM_ASSURED_HOSP_INCOME.doubleValue())))
            .andExpect(jsonPath("$.[*].sumAssuredCI").value(hasItem(DEFAULT_SUM_ASSURED_CI.doubleValue())))
            .andExpect(jsonPath("$.[*].sumAssuredMedic").value(hasItem(DEFAULT_SUM_ASSURED_MEDIC.doubleValue())))
            .andExpect(jsonPath("$.[*].sumAssuredCancer").value(hasItem(DEFAULT_SUM_ASSURED_CANCER.doubleValue())))
            .andExpect(jsonPath("$.[*].productWeightDeath").value(hasItem(DEFAULT_PRODUCT_WEIGHT_DEATH.doubleValue())))
            .andExpect(jsonPath("$.[*].productWeightPA").value(hasItem(DEFAULT_PRODUCT_WEIGHT_PA.doubleValue())))
            .andExpect(jsonPath("$.[*].productWeightHospIncome").value(hasItem(DEFAULT_PRODUCT_WEIGHT_HOSP_INCOME.doubleValue())))
            .andExpect(jsonPath("$.[*].productWeightCI").value(hasItem(DEFAULT_PRODUCT_WEIGHT_CI.doubleValue())))
            .andExpect(jsonPath("$.[*].productWeightMedic").value(hasItem(DEFAULT_PRODUCT_WEIGHT_MEDIC.doubleValue())))
            .andExpect(jsonPath("$.[*].productWeightCancer").value(hasItem(DEFAULT_PRODUCT_WEIGHT_CANCER.doubleValue())));
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
