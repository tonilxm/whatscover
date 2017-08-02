package com.whatscover.web.rest;

import com.whatscover.WhatscoverApp;

import com.whatscover.domain.InsuranceProductPremiumRate;
import com.whatscover.domain.InsuranceProduct;
import com.whatscover.repository.InsuranceProductPremiumRateRepository;
import com.whatscover.service.InsuranceProductPremiumRateService;
import com.whatscover.repository.search.InsuranceProductPremiumRateSearchRepository;
import com.whatscover.service.dto.InsuranceProductPremiumRateDTO;
import com.whatscover.service.mapper.InsuranceProductPremiumRateMapper;
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
 * Test class for the InsuranceProductPremiumRateResource REST controller.
 *
 * @see InsuranceProductPremiumRateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WhatscoverApp.class)
public class InsuranceProductPremiumRateResourceIntTest {

    private static final Integer DEFAULT_ENTRY_AGE = 1;
    private static final Integer UPDATED_ENTRY_AGE = 2;

    private static final Double DEFAULT_MALE_PREMIUM_RATE = 1D;
    private static final Double UPDATED_MALE_PREMIUM_RATE = 2D;

    private static final Double DEFAULT_FEMALE_PREMIUM_RATE = 1D;
    private static final Double UPDATED_FEMALE_PREMIUM_RATE = 2D;

    private static final String DEFAULT_PLAN = "AAAAA";
    private static final String UPDATED_PLAN = "BBBBB";

    @Autowired
    private InsuranceProductPremiumRateRepository insuranceProductPremiumRateRepository;

    @Autowired
    private InsuranceProductPremiumRateMapper insuranceProductPremiumRateMapper;

    @Autowired
    private InsuranceProductPremiumRateService insuranceProductPremiumRateService;

    @Autowired
    private InsuranceProductPremiumRateSearchRepository insuranceProductPremiumRateSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInsuranceProductPremiumRateMockMvc;

    private InsuranceProductPremiumRate insuranceProductPremiumRate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InsuranceProductPremiumRateResource insuranceProductPremiumRateResource = new InsuranceProductPremiumRateResource(insuranceProductPremiumRateService);
        this.restInsuranceProductPremiumRateMockMvc = MockMvcBuilders.standaloneSetup(insuranceProductPremiumRateResource)
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
    public static InsuranceProductPremiumRate createEntity(EntityManager em) {
        InsuranceProductPremiumRate insuranceProductPremiumRate = new InsuranceProductPremiumRate()
            .entryAge(DEFAULT_ENTRY_AGE)
            .malePremiumRate(DEFAULT_MALE_PREMIUM_RATE)
            .femalePremiumRate(DEFAULT_FEMALE_PREMIUM_RATE)
            .plan(DEFAULT_PLAN);
        // Add required entity
        InsuranceProduct insuranceProduct = InsuranceProductResourceIntTest.createEntity(em);
        em.persist(insuranceProduct);
        em.flush();
        insuranceProductPremiumRate.setInsuranceProduct(insuranceProduct);
        return insuranceProductPremiumRate;
    }

    @Before
    public void initTest() {
        insuranceProductPremiumRateSearchRepository.deleteAll();
        insuranceProductPremiumRate = createEntity(em);
    }

    @Test
    @Transactional
    public void createInsuranceProductPremiumRate() throws Exception {
        int databaseSizeBeforeCreate = insuranceProductPremiumRateRepository.findAll().size();

        // Create the InsuranceProductPremiumRate
        InsuranceProductPremiumRateDTO insuranceProductPremiumRateDTO = insuranceProductPremiumRateMapper.toDto(insuranceProductPremiumRate);
        restInsuranceProductPremiumRateMockMvc.perform(post("/api/insurance-product-premium-rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceProductPremiumRateDTO)))
            .andExpect(status().isCreated());

        // Validate the InsuranceProductPremiumRate in the database
        List<InsuranceProductPremiumRate> insuranceProductPremiumRateList = insuranceProductPremiumRateRepository.findAll();
        assertThat(insuranceProductPremiumRateList).hasSize(databaseSizeBeforeCreate + 1);
        InsuranceProductPremiumRate testInsuranceProductPremiumRate = insuranceProductPremiumRateList.get(insuranceProductPremiumRateList.size() - 1);
        assertThat(testInsuranceProductPremiumRate.getEntryAge()).isEqualTo(DEFAULT_ENTRY_AGE);
        assertThat(testInsuranceProductPremiumRate.getMalePremiumRate()).isEqualTo(DEFAULT_MALE_PREMIUM_RATE);
        assertThat(testInsuranceProductPremiumRate.getFemalePremiumRate()).isEqualTo(DEFAULT_FEMALE_PREMIUM_RATE);
        assertThat(testInsuranceProductPremiumRate.getPlan()).isEqualTo(DEFAULT_PLAN);

        // Validate the InsuranceProductPremiumRate in Elasticsearch
        InsuranceProductPremiumRate insuranceProductPremiumRateEs = insuranceProductPremiumRateSearchRepository.findOne(testInsuranceProductPremiumRate.getId());
        assertThat(insuranceProductPremiumRateEs).isEqualToComparingFieldByField(testInsuranceProductPremiumRate);
    }

    @Test
    @Transactional
    public void createInsuranceProductPremiumRateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = insuranceProductPremiumRateRepository.findAll().size();

        // Create the InsuranceProductPremiumRate with an existing ID
        insuranceProductPremiumRate.setId(1L);
        InsuranceProductPremiumRateDTO insuranceProductPremiumRateDTO = insuranceProductPremiumRateMapper.toDto(insuranceProductPremiumRate);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceProductPremiumRateMockMvc.perform(post("/api/insurance-product-premium-rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceProductPremiumRateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<InsuranceProductPremiumRate> insuranceProductPremiumRateList = insuranceProductPremiumRateRepository.findAll();
        assertThat(insuranceProductPremiumRateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEntryAgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceProductPremiumRateRepository.findAll().size();
        // set the field null
        insuranceProductPremiumRate.setEntryAge(null);

        // Create the InsuranceProductPremiumRate, which fails.
        InsuranceProductPremiumRateDTO insuranceProductPremiumRateDTO = insuranceProductPremiumRateMapper.toDto(insuranceProductPremiumRate);

        restInsuranceProductPremiumRateMockMvc.perform(post("/api/insurance-product-premium-rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceProductPremiumRateDTO)))
            .andExpect(status().isBadRequest());

        List<InsuranceProductPremiumRate> insuranceProductPremiumRateList = insuranceProductPremiumRateRepository.findAll();
        assertThat(insuranceProductPremiumRateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMalePremiumRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceProductPremiumRateRepository.findAll().size();
        // set the field null
        insuranceProductPremiumRate.setMalePremiumRate(null);

        // Create the InsuranceProductPremiumRate, which fails.
        InsuranceProductPremiumRateDTO insuranceProductPremiumRateDTO = insuranceProductPremiumRateMapper.toDto(insuranceProductPremiumRate);

        restInsuranceProductPremiumRateMockMvc.perform(post("/api/insurance-product-premium-rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceProductPremiumRateDTO)))
            .andExpect(status().isBadRequest());

        List<InsuranceProductPremiumRate> insuranceProductPremiumRateList = insuranceProductPremiumRateRepository.findAll();
        assertThat(insuranceProductPremiumRateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFemalePremiumRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceProductPremiumRateRepository.findAll().size();
        // set the field null
        insuranceProductPremiumRate.setFemalePremiumRate(null);

        // Create the InsuranceProductPremiumRate, which fails.
        InsuranceProductPremiumRateDTO insuranceProductPremiumRateDTO = insuranceProductPremiumRateMapper.toDto(insuranceProductPremiumRate);

        restInsuranceProductPremiumRateMockMvc.perform(post("/api/insurance-product-premium-rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceProductPremiumRateDTO)))
            .andExpect(status().isBadRequest());

        List<InsuranceProductPremiumRate> insuranceProductPremiumRateList = insuranceProductPremiumRateRepository.findAll();
        assertThat(insuranceProductPremiumRateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInsuranceProductPremiumRates() throws Exception {
        // Initialize the database
        insuranceProductPremiumRateRepository.saveAndFlush(insuranceProductPremiumRate);

        // Get all the insuranceProductPremiumRateList
        restInsuranceProductPremiumRateMockMvc.perform(get("/api/insurance-product-premium-rates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceProductPremiumRate.getId().intValue())))
            .andExpect(jsonPath("$.[*].entryAge").value(hasItem(DEFAULT_ENTRY_AGE)))
            .andExpect(jsonPath("$.[*].malePremiumRate").value(hasItem(DEFAULT_MALE_PREMIUM_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].femalePremiumRate").value(hasItem(DEFAULT_FEMALE_PREMIUM_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].plan").value(hasItem(DEFAULT_PLAN.toString())));
    }

    @Test
    @Transactional
    public void getInsuranceProductPremiumRate() throws Exception {
        // Initialize the database
        insuranceProductPremiumRateRepository.saveAndFlush(insuranceProductPremiumRate);

        // Get the insuranceProductPremiumRate
        restInsuranceProductPremiumRateMockMvc.perform(get("/api/insurance-product-premium-rates/{id}", insuranceProductPremiumRate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(insuranceProductPremiumRate.getId().intValue()))
            .andExpect(jsonPath("$.entryAge").value(DEFAULT_ENTRY_AGE))
            .andExpect(jsonPath("$.malePremiumRate").value(DEFAULT_MALE_PREMIUM_RATE.doubleValue()))
            .andExpect(jsonPath("$.femalePremiumRate").value(DEFAULT_FEMALE_PREMIUM_RATE.doubleValue()))
            .andExpect(jsonPath("$.plan").value(DEFAULT_PLAN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInsuranceProductPremiumRate() throws Exception {
        // Get the insuranceProductPremiumRate
        restInsuranceProductPremiumRateMockMvc.perform(get("/api/insurance-product-premium-rates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInsuranceProductPremiumRate() throws Exception {
        // Initialize the database
        insuranceProductPremiumRateRepository.saveAndFlush(insuranceProductPremiumRate);
        insuranceProductPremiumRateSearchRepository.save(insuranceProductPremiumRate);
        int databaseSizeBeforeUpdate = insuranceProductPremiumRateRepository.findAll().size();

        // Update the insuranceProductPremiumRate
        InsuranceProductPremiumRate updatedInsuranceProductPremiumRate = insuranceProductPremiumRateRepository.findOne(insuranceProductPremiumRate.getId());
        updatedInsuranceProductPremiumRate
            .entryAge(UPDATED_ENTRY_AGE)
            .malePremiumRate(UPDATED_MALE_PREMIUM_RATE)
            .femalePremiumRate(UPDATED_FEMALE_PREMIUM_RATE)
            .plan(UPDATED_PLAN);
        InsuranceProductPremiumRateDTO insuranceProductPremiumRateDTO = insuranceProductPremiumRateMapper.toDto(updatedInsuranceProductPremiumRate);

        restInsuranceProductPremiumRateMockMvc.perform(put("/api/insurance-product-premium-rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceProductPremiumRateDTO)))
            .andExpect(status().isOk());

        // Validate the InsuranceProductPremiumRate in the database
        List<InsuranceProductPremiumRate> insuranceProductPremiumRateList = insuranceProductPremiumRateRepository.findAll();
        assertThat(insuranceProductPremiumRateList).hasSize(databaseSizeBeforeUpdate);
        InsuranceProductPremiumRate testInsuranceProductPremiumRate = insuranceProductPremiumRateList.get(insuranceProductPremiumRateList.size() - 1);
        assertThat(testInsuranceProductPremiumRate.getEntryAge()).isEqualTo(UPDATED_ENTRY_AGE);
        assertThat(testInsuranceProductPremiumRate.getMalePremiumRate()).isEqualTo(UPDATED_MALE_PREMIUM_RATE);
        assertThat(testInsuranceProductPremiumRate.getFemalePremiumRate()).isEqualTo(UPDATED_FEMALE_PREMIUM_RATE);
        assertThat(testInsuranceProductPremiumRate.getPlan()).isEqualTo(UPDATED_PLAN);

        // Validate the InsuranceProductPremiumRate in Elasticsearch
        InsuranceProductPremiumRate insuranceProductPremiumRateEs = insuranceProductPremiumRateSearchRepository.findOne(testInsuranceProductPremiumRate.getId());
        assertThat(insuranceProductPremiumRateEs).isEqualToComparingFieldByField(testInsuranceProductPremiumRate);
    }

    @Test
    @Transactional
    public void updateNonExistingInsuranceProductPremiumRate() throws Exception {
        int databaseSizeBeforeUpdate = insuranceProductPremiumRateRepository.findAll().size();

        // Create the InsuranceProductPremiumRate
        InsuranceProductPremiumRateDTO insuranceProductPremiumRateDTO = insuranceProductPremiumRateMapper.toDto(insuranceProductPremiumRate);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInsuranceProductPremiumRateMockMvc.perform(put("/api/insurance-product-premium-rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceProductPremiumRateDTO)))
            .andExpect(status().isCreated());

        // Validate the InsuranceProductPremiumRate in the database
        List<InsuranceProductPremiumRate> insuranceProductPremiumRateList = insuranceProductPremiumRateRepository.findAll();
        assertThat(insuranceProductPremiumRateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInsuranceProductPremiumRate() throws Exception {
        // Initialize the database
        insuranceProductPremiumRateRepository.saveAndFlush(insuranceProductPremiumRate);
        insuranceProductPremiumRateSearchRepository.save(insuranceProductPremiumRate);
        int databaseSizeBeforeDelete = insuranceProductPremiumRateRepository.findAll().size();

        // Get the insuranceProductPremiumRate
        restInsuranceProductPremiumRateMockMvc.perform(delete("/api/insurance-product-premium-rates/{id}", insuranceProductPremiumRate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean insuranceProductPremiumRateExistsInEs = insuranceProductPremiumRateSearchRepository.exists(insuranceProductPremiumRate.getId());
        assertThat(insuranceProductPremiumRateExistsInEs).isFalse();

        // Validate the database is empty
        List<InsuranceProductPremiumRate> insuranceProductPremiumRateList = insuranceProductPremiumRateRepository.findAll();
        assertThat(insuranceProductPremiumRateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInsuranceProductPremiumRate() throws Exception {
        // Initialize the database
        insuranceProductPremiumRateRepository.saveAndFlush(insuranceProductPremiumRate);
        insuranceProductPremiumRateSearchRepository.save(insuranceProductPremiumRate);

        // Search the insuranceProductPremiumRate
        restInsuranceProductPremiumRateMockMvc.perform(get("/api/_search/insurance-product-premium-rates?query=id:" + insuranceProductPremiumRate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceProductPremiumRate.getId().intValue())))
            .andExpect(jsonPath("$.[*].entryAge").value(hasItem(DEFAULT_ENTRY_AGE)))
            .andExpect(jsonPath("$.[*].malePremiumRate").value(hasItem(DEFAULT_MALE_PREMIUM_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].femalePremiumRate").value(hasItem(DEFAULT_FEMALE_PREMIUM_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].plan").value(hasItem(DEFAULT_PLAN.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceProductPremiumRate.class);
        InsuranceProductPremiumRate insuranceProductPremiumRate1 = new InsuranceProductPremiumRate();
        insuranceProductPremiumRate1.setId(1L);
        InsuranceProductPremiumRate insuranceProductPremiumRate2 = new InsuranceProductPremiumRate();
        insuranceProductPremiumRate2.setId(insuranceProductPremiumRate1.getId());
        assertThat(insuranceProductPremiumRate1).isEqualTo(insuranceProductPremiumRate2);
        insuranceProductPremiumRate2.setId(2L);
        assertThat(insuranceProductPremiumRate1).isNotEqualTo(insuranceProductPremiumRate2);
        insuranceProductPremiumRate1.setId(null);
        assertThat(insuranceProductPremiumRate1).isNotEqualTo(insuranceProductPremiumRate2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceProductPremiumRateDTO.class);
        InsuranceProductPremiumRateDTO insuranceProductPremiumRateDTO1 = new InsuranceProductPremiumRateDTO();
        insuranceProductPremiumRateDTO1.setId(1L);
        InsuranceProductPremiumRateDTO insuranceProductPremiumRateDTO2 = new InsuranceProductPremiumRateDTO();
        assertThat(insuranceProductPremiumRateDTO1).isNotEqualTo(insuranceProductPremiumRateDTO2);
        insuranceProductPremiumRateDTO2.setId(insuranceProductPremiumRateDTO1.getId());
        assertThat(insuranceProductPremiumRateDTO1).isEqualTo(insuranceProductPremiumRateDTO2);
        insuranceProductPremiumRateDTO2.setId(2L);
        assertThat(insuranceProductPremiumRateDTO1).isNotEqualTo(insuranceProductPremiumRateDTO2);
        insuranceProductPremiumRateDTO1.setId(null);
        assertThat(insuranceProductPremiumRateDTO1).isNotEqualTo(insuranceProductPremiumRateDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(insuranceProductPremiumRateMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(insuranceProductPremiumRateMapper.fromId(null)).isNull();
    }
}
