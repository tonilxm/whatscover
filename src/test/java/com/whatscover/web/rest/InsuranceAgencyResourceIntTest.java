package com.whatscover.web.rest;

import com.whatscover.WhatscoverApp;

import com.whatscover.domain.InsuranceAgency;
import com.whatscover.repository.InsuranceAgencyRepository;
import com.whatscover.repository.search.InsuranceAgencySearchRepository;
import com.whatscover.service.InsuranceAgencyService;
import com.whatscover.service.dto.InsuranceAgencyDTO;
import com.whatscover.service.mapper.InsuranceAgencyMapper;
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
 * Test class for the InsuranceAgencyResource REST controller.
 *
 * @see InsuranceAgencyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WhatscoverApp.class)
public class InsuranceAgencyResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private InsuranceAgencyService insuranceAgencyService;
    
    @Autowired
    private InsuranceAgencyRepository insuranceAgencyRepository;

    @Autowired
    private InsuranceAgencyMapper insuranceAgencyMapper;

    @Autowired
    private InsuranceAgencySearchRepository insuranceAgencySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInsuranceAgencyMockMvc;

    private InsuranceAgency insuranceAgency;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InsuranceAgencyResource insuranceAgencyResource = new InsuranceAgencyResource(
        		insuranceAgencyService, insuranceAgencyRepository);
        this.restInsuranceAgencyMockMvc = MockMvcBuilders.standaloneSetup(insuranceAgencyResource)
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
    public static InsuranceAgency createEntity(EntityManager em) {
        InsuranceAgency insuranceAgency = new InsuranceAgency()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS);
        return insuranceAgency;
    }

    @Before
    public void initTest() {
        insuranceAgencySearchRepository.deleteAll();
        insuranceAgency = createEntity(em);
    }

    @Test
    @Transactional
    public void createInsuranceAgency() throws Exception {
        int databaseSizeBeforeCreate = insuranceAgencyRepository.findAll().size();

        // Create the InsuranceAgency
        InsuranceAgencyDTO insuranceAgencyDTO = insuranceAgencyMapper.toDto(insuranceAgency);
        restInsuranceAgencyMockMvc.perform(post("/api/insurance-agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceAgencyDTO)))
            .andExpect(status().isCreated());

        // Validate the InsuranceAgency in the database
        List<InsuranceAgency> insuranceAgencyList = insuranceAgencyRepository.findAll();
        assertThat(insuranceAgencyList).hasSize(databaseSizeBeforeCreate + 1);
        InsuranceAgency testInsuranceAgency = insuranceAgencyList.get(insuranceAgencyList.size() - 1);
        assertThat(testInsuranceAgency.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testInsuranceAgency.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInsuranceAgency.getAddress()).isEqualTo(DEFAULT_ADDRESS);

        // Validate the InsuranceAgency in Elasticsearch
        InsuranceAgency insuranceAgencyEs = insuranceAgencySearchRepository.findOne(testInsuranceAgency.getId());
        assertThat(insuranceAgencyEs.equals(testInsuranceAgency));
    }

    @Test
    @Transactional
    public void createInsuranceAgencyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = insuranceAgencyRepository.findAll().size();

        // Create the InsuranceAgency with an existing ID
        insuranceAgency.setId(1L);
        InsuranceAgencyDTO insuranceAgencyDTO = insuranceAgencyMapper.toDto(insuranceAgency);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceAgencyMockMvc.perform(post("/api/insurance-agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceAgencyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<InsuranceAgency> insuranceAgencyList = insuranceAgencyRepository.findAll();
        assertThat(insuranceAgencyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceAgencyRepository.findAll().size();
        // set the field null
        insuranceAgency.setCode(null);

        // Create the InsuranceAgency, which fails.
        InsuranceAgencyDTO insuranceAgencyDTO = insuranceAgencyMapper.toDto(insuranceAgency);

        restInsuranceAgencyMockMvc.perform(post("/api/insurance-agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceAgencyDTO)))
            .andExpect(status().isBadRequest());

        List<InsuranceAgency> insuranceAgencyList = insuranceAgencyRepository.findAll();
        assertThat(insuranceAgencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInsuranceAgencies() throws Exception {
        // Initialize the database
        insuranceAgencyRepository.saveAndFlush(insuranceAgency);

        // Get all the insuranceAgencyList
        restInsuranceAgencyMockMvc.perform(get("/api/insurance-agencies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceAgency.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }

    @Test
    @Transactional
    public void getInsuranceAgency() throws Exception {
        // Initialize the database
        insuranceAgencyRepository.saveAndFlush(insuranceAgency);

        // Get the insuranceAgency
        restInsuranceAgencyMockMvc.perform(get("/api/insurance-agencies/{id}", insuranceAgency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(insuranceAgency.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInsuranceAgency() throws Exception {
        // Get the insuranceAgency
        restInsuranceAgencyMockMvc.perform(get("/api/insurance-agencies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInsuranceAgency() throws Exception {
        // Initialize the database
        insuranceAgencyRepository.saveAndFlush(insuranceAgency);
        insuranceAgencySearchRepository.save(insuranceAgency);
        int databaseSizeBeforeUpdate = insuranceAgencyRepository.findAll().size();

        // Update the insuranceAgency
        InsuranceAgency updatedInsuranceAgency = insuranceAgencyRepository.findOne(insuranceAgency.getId());
        updatedInsuranceAgency
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS);
        InsuranceAgencyDTO insuranceAgencyDTO = insuranceAgencyMapper.toDto(updatedInsuranceAgency);

        restInsuranceAgencyMockMvc.perform(put("/api/insurance-agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceAgencyDTO)))
            .andExpect(status().isOk());

        // Validate the InsuranceAgency in the database
        List<InsuranceAgency> insuranceAgencyList = insuranceAgencyRepository.findAll();
        assertThat(insuranceAgencyList).hasSize(databaseSizeBeforeUpdate);
        InsuranceAgency testInsuranceAgency = insuranceAgencyList.get(insuranceAgencyList.size() - 1);
        assertThat(testInsuranceAgency.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testInsuranceAgency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInsuranceAgency.getAddress()).isEqualTo(UPDATED_ADDRESS);

        // Validate the InsuranceAgency in Elasticsearch
        InsuranceAgency insuranceAgencyEs = insuranceAgencySearchRepository.findOne(testInsuranceAgency.getId());
        assertThat(insuranceAgencyEs.equals(testInsuranceAgency));
    }

    @Test
    @Transactional
    public void updateNonExistingInsuranceAgency() throws Exception {
        int databaseSizeBeforeUpdate = insuranceAgencyRepository.findAll().size();

        // Create the InsuranceAgency
        InsuranceAgencyDTO insuranceAgencyDTO = insuranceAgencyMapper.toDto(insuranceAgency);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInsuranceAgencyMockMvc.perform(put("/api/insurance-agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceAgencyDTO)))
            .andExpect(status().isCreated());

        // Validate the InsuranceAgency in the database
        List<InsuranceAgency> insuranceAgencyList = insuranceAgencyRepository.findAll();
        assertThat(insuranceAgencyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInsuranceAgency() throws Exception {
        // Initialize the database
        insuranceAgencyRepository.saveAndFlush(insuranceAgency);
        insuranceAgencySearchRepository.save(insuranceAgency);
        int databaseSizeBeforeDelete = insuranceAgencyRepository.findAll().size();

        // Get the insuranceAgency
        restInsuranceAgencyMockMvc.perform(delete("/api/insurance-agencies/{id}", insuranceAgency.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean insuranceAgencyExistsInEs = insuranceAgencySearchRepository.exists(insuranceAgency.getId());
        assertThat(insuranceAgencyExistsInEs).isFalse();

        // Validate the database is empty
        List<InsuranceAgency> insuranceAgencyList = insuranceAgencyRepository.findAll();
        assertThat(insuranceAgencyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInsuranceAgency() throws Exception {
        // Initialize the database
        insuranceAgencyRepository.saveAndFlush(insuranceAgency);
        insuranceAgencySearchRepository.save(insuranceAgency);

        // Search the insuranceAgency
        restInsuranceAgencyMockMvc.perform(get("/api/_search/insurance-agencies?query=id:" + insuranceAgency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceAgency.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceAgency.class);
        InsuranceAgency insuranceAgency1 = new InsuranceAgency();
        insuranceAgency1.setId(1L);
        InsuranceAgency insuranceAgency2 = new InsuranceAgency();
        insuranceAgency2.setId(insuranceAgency1.getId());
        assertThat(insuranceAgency1).isEqualTo(insuranceAgency2);
        insuranceAgency2.setId(2L);
        assertThat(insuranceAgency1).isNotEqualTo(insuranceAgency2);
        insuranceAgency1.setId(null);
        assertThat(insuranceAgency1).isNotEqualTo(insuranceAgency2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceAgencyDTO.class);
        InsuranceAgencyDTO insuranceAgencyDTO1 = new InsuranceAgencyDTO();
        insuranceAgencyDTO1.setId(1L);
        InsuranceAgencyDTO insuranceAgencyDTO2 = new InsuranceAgencyDTO();
        assertThat(insuranceAgencyDTO1).isNotEqualTo(insuranceAgencyDTO2);
        insuranceAgencyDTO2.setId(insuranceAgencyDTO1.getId());
        assertThat(insuranceAgencyDTO1).isEqualTo(insuranceAgencyDTO2);
        insuranceAgencyDTO2.setId(2L);
        assertThat(insuranceAgencyDTO1).isNotEqualTo(insuranceAgencyDTO2);
        insuranceAgencyDTO1.setId(null);
        assertThat(insuranceAgencyDTO1).isNotEqualTo(insuranceAgencyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(insuranceAgencyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(insuranceAgencyMapper.fromId(null)).isNull();
    }
}
