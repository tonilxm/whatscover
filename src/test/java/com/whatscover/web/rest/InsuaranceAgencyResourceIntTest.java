package com.whatscover.web.rest;

import com.whatscover.WhatscoverApp;

import com.whatscover.domain.InsuaranceAgency;
import com.whatscover.repository.InsuaranceAgencyRepository;
import com.whatscover.repository.search.InsuaranceAgencySearchRepository;
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
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InsuaranceAgencyResource REST controller.
 *
 * @see InsuaranceAgencyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WhatscoverApp.class)
public class InsuaranceAgencyResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_INSUARANCE_COMPANY_ID = 1;
    private static final Integer UPDATED_INSUARANCE_COMPANY_ID = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_3 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_3 = "BBBBBBBBBB";
    
    @Autowired
    private InsuaranceAgencyRepository insuaranceAgencyRepository;

    @Autowired
    private InsuaranceAgencySearchRepository insuaranceAgencySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInsuaranceAgencyMockMvc;

    private InsuaranceAgency insuaranceAgency;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InsuaranceAgencyResource insuaranceAgencyResource = new InsuaranceAgencyResource(insuaranceAgencyRepository, insuaranceAgencySearchRepository);
        this.restInsuaranceAgencyMockMvc = MockMvcBuilders.standaloneSetup(insuaranceAgencyResource)
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
    public static InsuaranceAgency createEntity(EntityManager em) {
        InsuaranceAgency insuaranceAgency = new InsuaranceAgency()
            .code(DEFAULT_CODE)
            .insuarance_company_id(DEFAULT_INSUARANCE_COMPANY_ID)
            .name(DEFAULT_NAME)
            .address_1(DEFAULT_ADDRESS_1)
            .address_2(DEFAULT_ADDRESS_2)
            .address_3(DEFAULT_ADDRESS_3);
        return insuaranceAgency;
    }

    @Before
    public void initTest() {
        insuaranceAgencySearchRepository.deleteAll();
        insuaranceAgency = createEntity(em);
    }

    @Test
    @Transactional
    public void createInsuaranceAgency() throws Exception {
        int databaseSizeBeforeCreate = insuaranceAgencyRepository.findAll().size();

        // Create the InsuaranceAgency
        restInsuaranceAgencyMockMvc.perform(post("/api/insuarance-agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuaranceAgency)))
            .andExpect(status().isCreated());

        // Validate the InsuaranceAgency in the database
        List<InsuaranceAgency> insuaranceAgencyList = insuaranceAgencyRepository.findAll();
        assertThat(insuaranceAgencyList).hasSize(databaseSizeBeforeCreate + 1);
        InsuaranceAgency testInsuaranceAgency = insuaranceAgencyList.get(insuaranceAgencyList.size() - 1);
        assertThat(testInsuaranceAgency.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testInsuaranceAgency.getInsuarance_company_id()).isEqualTo(DEFAULT_INSUARANCE_COMPANY_ID);
        assertThat(testInsuaranceAgency.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInsuaranceAgency.getAddress_1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testInsuaranceAgency.getAddress_2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testInsuaranceAgency.getAddress_3()).isEqualTo(DEFAULT_ADDRESS_3);

        // Validate the InsuaranceAgency in Elasticsearch
        InsuaranceAgency insuaranceAgencyEs = insuaranceAgencySearchRepository.findOne(testInsuaranceAgency.getId());
        assertTrue(insuaranceAgencyEs.equals(testInsuaranceAgency));
    }

    @Test
    @Transactional
    public void createInsuaranceAgencyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = insuaranceAgencyRepository.findAll().size();

        // Create the InsuaranceAgency with an existing ID
        insuaranceAgency.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuaranceAgencyMockMvc.perform(post("/api/insuarance-agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuaranceAgency)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<InsuaranceAgency> insuaranceAgencyList = insuaranceAgencyRepository.findAll();
        assertThat(insuaranceAgencyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuaranceAgencyRepository.findAll().size();
        // set the field null
        insuaranceAgency.setCode(null);

        // Create the InsuaranceAgency, which fails.

        restInsuaranceAgencyMockMvc.perform(post("/api/insuarance-agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuaranceAgency)))
            .andExpect(status().isBadRequest());

        List<InsuaranceAgency> insuaranceAgencyList = insuaranceAgencyRepository.findAll();
        assertThat(insuaranceAgencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInsuarance_company_idIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuaranceAgencyRepository.findAll().size();
        // set the field null
        insuaranceAgency.setInsuarance_company_id(null);

        // Create the InsuaranceAgency, which fails.

        restInsuaranceAgencyMockMvc.perform(post("/api/insuarance-agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuaranceAgency)))
            .andExpect(status().isBadRequest());

        List<InsuaranceAgency> insuaranceAgencyList = insuaranceAgencyRepository.findAll();
        assertThat(insuaranceAgencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInsuaranceAgencies() throws Exception {
        // Initialize the database
        insuaranceAgencyRepository.saveAndFlush(insuaranceAgency);

        // Get all the insuaranceAgencyList
        restInsuaranceAgencyMockMvc.perform(get("/api/insuarance-agencies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuaranceAgency.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].insuarance_company_id").value(hasItem(DEFAULT_INSUARANCE_COMPANY_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].address_1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
            .andExpect(jsonPath("$.[*].address_2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
            .andExpect(jsonPath("$.[*].address_3").value(hasItem(DEFAULT_ADDRESS_3.toString())));
    }

    @Test
    @Transactional
    public void getInsuaranceAgency() throws Exception {
        // Initialize the database
        insuaranceAgencyRepository.saveAndFlush(insuaranceAgency);

        // Get the insuaranceAgency
        restInsuaranceAgencyMockMvc.perform(get("/api/insuarance-agencies/{id}", insuaranceAgency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(insuaranceAgency.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.insuarance_company_id").value(DEFAULT_INSUARANCE_COMPANY_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.address_1").value(DEFAULT_ADDRESS_1.toString()))
            .andExpect(jsonPath("$.address_2").value(DEFAULT_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.address_3").value(DEFAULT_ADDRESS_3.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInsuaranceAgency() throws Exception {
        // Get the insuaranceAgency
        restInsuaranceAgencyMockMvc.perform(get("/api/insuarance-agencies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInsuaranceAgency() throws Exception {
        // Initialize the database
        insuaranceAgencyRepository.saveAndFlush(insuaranceAgency);
        insuaranceAgencySearchRepository.save(insuaranceAgency);
        int databaseSizeBeforeUpdate = insuaranceAgencyRepository.findAll().size();

        // Update the insuaranceAgency
        InsuaranceAgency updatedInsuaranceAgency = insuaranceAgencyRepository.findOne(insuaranceAgency.getId());
        updatedInsuaranceAgency
            .code(UPDATED_CODE)
            .insuarance_company_id(UPDATED_INSUARANCE_COMPANY_ID)
            .name(UPDATED_NAME)
            .address_1(UPDATED_ADDRESS_1)
            .address_2(UPDATED_ADDRESS_2)
            .address_3(UPDATED_ADDRESS_3);

        restInsuaranceAgencyMockMvc.perform(put("/api/insuarance-agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInsuaranceAgency)))
            .andExpect(status().isOk());

        // Validate the InsuaranceAgency in the database
        List<InsuaranceAgency> insuaranceAgencyList = insuaranceAgencyRepository.findAll();
        assertThat(insuaranceAgencyList).hasSize(databaseSizeBeforeUpdate);
        InsuaranceAgency testInsuaranceAgency = insuaranceAgencyList.get(insuaranceAgencyList.size() - 1);
        assertThat(testInsuaranceAgency.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testInsuaranceAgency.getInsuarance_company_id()).isEqualTo(UPDATED_INSUARANCE_COMPANY_ID);
        assertThat(testInsuaranceAgency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInsuaranceAgency.getAddress_1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testInsuaranceAgency.getAddress_2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testInsuaranceAgency.getAddress_3()).isEqualTo(UPDATED_ADDRESS_3);

        // Validate the InsuaranceAgency in Elasticsearch
        InsuaranceAgency insuaranceAgencyEs = insuaranceAgencySearchRepository.findOne(testInsuaranceAgency.getId());
        assertTrue(insuaranceAgencyEs.equals(testInsuaranceAgency));
    }

    @Test
    @Transactional
    public void updateNonExistingInsuaranceAgency() throws Exception {
        int databaseSizeBeforeUpdate = insuaranceAgencyRepository.findAll().size();

        // Create the InsuaranceAgency

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInsuaranceAgencyMockMvc.perform(put("/api/insuarance-agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuaranceAgency)))
            .andExpect(status().isCreated());

        // Validate the InsuaranceAgency in the database
        List<InsuaranceAgency> insuaranceAgencyList = insuaranceAgencyRepository.findAll();
        assertThat(insuaranceAgencyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInsuaranceAgency() throws Exception {
        // Initialize the database
        insuaranceAgencyRepository.saveAndFlush(insuaranceAgency);
        insuaranceAgencySearchRepository.save(insuaranceAgency);
        int databaseSizeBeforeDelete = insuaranceAgencyRepository.findAll().size();

        // Get the insuaranceAgency
        restInsuaranceAgencyMockMvc.perform(delete("/api/insuarance-agencies/{id}", insuaranceAgency.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean insuaranceAgencyExistsInEs = insuaranceAgencySearchRepository.exists(insuaranceAgency.getId());
        assertThat(insuaranceAgencyExistsInEs).isFalse();

        // Validate the database is empty
        List<InsuaranceAgency> insuaranceAgencyList = insuaranceAgencyRepository.findAll();
        assertThat(insuaranceAgencyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInsuaranceAgency() throws Exception {
        // Initialize the database
        insuaranceAgencyRepository.saveAndFlush(insuaranceAgency);
        insuaranceAgencySearchRepository.save(insuaranceAgency);

        // Search the insuaranceAgency
        restInsuaranceAgencyMockMvc.perform(get("/api/_search/insuarance-agencies?query=id:" + insuaranceAgency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuaranceAgency.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].insuarance_company_id").value(hasItem(DEFAULT_INSUARANCE_COMPANY_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].address_1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
            .andExpect(jsonPath("$.[*].address_2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
            .andExpect(jsonPath("$.[*].address_3").value(hasItem(DEFAULT_ADDRESS_3.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuaranceAgency.class);
        InsuaranceAgency insuaranceAgency1 = new InsuaranceAgency();
        insuaranceAgency1.setId(1L);
        InsuaranceAgency insuaranceAgency2 = new InsuaranceAgency();
        insuaranceAgency2.setId(insuaranceAgency1.getId());
        assertThat(insuaranceAgency1).isEqualTo(insuaranceAgency2);
        insuaranceAgency2.setId(2L);
        assertThat(insuaranceAgency1).isNotEqualTo(insuaranceAgency2);
        insuaranceAgency1.setId(null);
        assertThat(insuaranceAgency1).isNotEqualTo(insuaranceAgency2);
    }
}
