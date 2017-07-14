package com.whatscover.web.rest;

import com.whatscover.WhatscoverApp;

import com.whatscover.domain.InsuranceCompany;
import com.whatscover.repository.InsuranceCompanyRepository;
import com.whatscover.service.InsuranceCompanyService;
import com.whatscover.repository.search.InsuranceCompanySearchRepository;
import com.whatscover.service.dto.InsuranceCompanyDTO;
import com.whatscover.service.mapper.InsuranceCompanyMapper;
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
 * Test class for the InsuranceCompanyResource REST controller.
 *
 * @see InsuranceCompanyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WhatscoverApp.class)
public class InsuranceCompanyResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_3 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_3 = "BBBBBBBBBB";

    @Autowired
    private InsuranceCompanyRepository insuranceCompanyRepository;

    @Autowired
    private InsuranceCompanyMapper insuranceCompanyMapper;

    @Autowired
    private InsuranceCompanyService insuranceCompanyService;

    @Autowired
    private InsuranceCompanySearchRepository insuranceCompanySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInsuranceCompanyMockMvc;

    private InsuranceCompany insuranceCompany;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InsuranceCompanyResource insuranceCompanyResource = new InsuranceCompanyResource(insuranceCompanyService);
        this.restInsuranceCompanyMockMvc = MockMvcBuilders.standaloneSetup(insuranceCompanyResource)
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
    public static InsuranceCompany createEntity(EntityManager em) {
        InsuranceCompany insuranceCompany = new InsuranceCompany()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .address_1(DEFAULT_ADDRESS_1)
            .address_2(DEFAULT_ADDRESS_2)
            .address_3(DEFAULT_ADDRESS_3);
        return insuranceCompany;
    }

    @Before
    public void initTest() {
        insuranceCompanySearchRepository.deleteAll();
        insuranceCompany = createEntity(em);
    }

    @Test
    @Transactional
    public void createInsuranceCompany() throws Exception {
        int databaseSizeBeforeCreate = insuranceCompanyRepository.findAll().size();

        // Create the InsuranceCompany
        InsuranceCompanyDTO insuranceCompanyDTO = insuranceCompanyMapper.toDto(insuranceCompany);
        restInsuranceCompanyMockMvc.perform(post("/api/insurance-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceCompanyDTO)))
            .andExpect(status().isCreated());

        // Validate the InsuranceCompany in the database
        List<InsuranceCompany> insuranceCompanyList = insuranceCompanyRepository.findAll();
        assertThat(insuranceCompanyList).hasSize(databaseSizeBeforeCreate + 1);
        InsuranceCompany testInsuranceCompany = insuranceCompanyList.get(insuranceCompanyList.size() - 1);
        assertThat(testInsuranceCompany.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testInsuranceCompany.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInsuranceCompany.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInsuranceCompany.getAddress_1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testInsuranceCompany.getAddress_2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testInsuranceCompany.getAddress_3()).isEqualTo(DEFAULT_ADDRESS_3);

        // Validate the InsuranceCompany in Elasticsearch
        InsuranceCompany insuranceCompanyEs = insuranceCompanySearchRepository.findOne(testInsuranceCompany.getId());
        assertThat(insuranceCompanyEs).isEqualToComparingFieldByField(testInsuranceCompany);
    }

    @Test
    @Transactional
    public void createInsuranceCompanyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = insuranceCompanyRepository.findAll().size();

        // Create the InsuranceCompany with an existing ID
        insuranceCompany.setId(1L);
        InsuranceCompanyDTO insuranceCompanyDTO = insuranceCompanyMapper.toDto(insuranceCompany);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceCompanyMockMvc.perform(post("/api/insurance-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceCompanyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<InsuranceCompany> insuranceCompanyList = insuranceCompanyRepository.findAll();
        assertThat(insuranceCompanyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceCompanyRepository.findAll().size();
        // set the field null
        insuranceCompany.setCode(null);

        // Create the InsuranceCompany, which fails.
        InsuranceCompanyDTO insuranceCompanyDTO = insuranceCompanyMapper.toDto(insuranceCompany);

        restInsuranceCompanyMockMvc.perform(post("/api/insurance-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceCompanyDTO)))
            .andExpect(status().isBadRequest());

        List<InsuranceCompany> insuranceCompanyList = insuranceCompanyRepository.findAll();
        assertThat(insuranceCompanyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInsuranceCompanies() throws Exception {
        // Initialize the database
        insuranceCompanyRepository.saveAndFlush(insuranceCompany);

        // Get all the insuranceCompanyList
        restInsuranceCompanyMockMvc.perform(get("/api/insurance-companies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceCompany.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].address_1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
            .andExpect(jsonPath("$.[*].address_2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
            .andExpect(jsonPath("$.[*].address_3").value(hasItem(DEFAULT_ADDRESS_3.toString())));
    }

    @Test
    @Transactional
    public void getInsuranceCompany() throws Exception {
        // Initialize the database
        insuranceCompanyRepository.saveAndFlush(insuranceCompany);

        // Get the insuranceCompany
        restInsuranceCompanyMockMvc.perform(get("/api/insurance-companies/{id}", insuranceCompany.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(insuranceCompany.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.address_1").value(DEFAULT_ADDRESS_1.toString()))
            .andExpect(jsonPath("$.address_2").value(DEFAULT_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.address_3").value(DEFAULT_ADDRESS_3.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInsuranceCompany() throws Exception {
        // Get the insuranceCompany
        restInsuranceCompanyMockMvc.perform(get("/api/insurance-companies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInsuranceCompany() throws Exception {
        // Initialize the database
        insuranceCompanyRepository.saveAndFlush(insuranceCompany);
        insuranceCompanySearchRepository.save(insuranceCompany);
        int databaseSizeBeforeUpdate = insuranceCompanyRepository.findAll().size();

        // Update the insuranceCompany
        InsuranceCompany updatedInsuranceCompany = insuranceCompanyRepository.findOne(insuranceCompany.getId());
        updatedInsuranceCompany
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .address_1(UPDATED_ADDRESS_1)
            .address_2(UPDATED_ADDRESS_2)
            .address_3(UPDATED_ADDRESS_3);
        InsuranceCompanyDTO insuranceCompanyDTO = insuranceCompanyMapper.toDto(updatedInsuranceCompany);

        restInsuranceCompanyMockMvc.perform(put("/api/insurance-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceCompanyDTO)))
            .andExpect(status().isOk());

        // Validate the InsuranceCompany in the database
        List<InsuranceCompany> insuranceCompanyList = insuranceCompanyRepository.findAll();
        assertThat(insuranceCompanyList).hasSize(databaseSizeBeforeUpdate);
        InsuranceCompany testInsuranceCompany = insuranceCompanyList.get(insuranceCompanyList.size() - 1);
        assertThat(testInsuranceCompany.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testInsuranceCompany.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInsuranceCompany.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInsuranceCompany.getAddress_1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testInsuranceCompany.getAddress_2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testInsuranceCompany.getAddress_3()).isEqualTo(UPDATED_ADDRESS_3);

        // Validate the InsuranceCompany in Elasticsearch
        InsuranceCompany insuranceCompanyEs = insuranceCompanySearchRepository.findOne(testInsuranceCompany.getId());
        assertThat(insuranceCompanyEs).isEqualToComparingFieldByField(testInsuranceCompany);
    }

    @Test
    @Transactional
    public void updateNonExistingInsuranceCompany() throws Exception {
        int databaseSizeBeforeUpdate = insuranceCompanyRepository.findAll().size();

        // Create the InsuranceCompany
        InsuranceCompanyDTO insuranceCompanyDTO = insuranceCompanyMapper.toDto(insuranceCompany);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInsuranceCompanyMockMvc.perform(put("/api/insurance-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insuranceCompanyDTO)))
            .andExpect(status().isCreated());

        // Validate the InsuranceCompany in the database
        List<InsuranceCompany> insuranceCompanyList = insuranceCompanyRepository.findAll();
        assertThat(insuranceCompanyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInsuranceCompany() throws Exception {
        // Initialize the database
        insuranceCompanyRepository.saveAndFlush(insuranceCompany);
        insuranceCompanySearchRepository.save(insuranceCompany);
        int databaseSizeBeforeDelete = insuranceCompanyRepository.findAll().size();

        // Get the insuranceCompany
        restInsuranceCompanyMockMvc.perform(delete("/api/insurance-companies/{id}", insuranceCompany.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean insuranceCompanyExistsInEs = insuranceCompanySearchRepository.exists(insuranceCompany.getId());
        assertThat(insuranceCompanyExistsInEs).isFalse();

        // Validate the database is empty
        List<InsuranceCompany> insuranceCompanyList = insuranceCompanyRepository.findAll();
        assertThat(insuranceCompanyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInsuranceCompany() throws Exception {
        // Initialize the database
        insuranceCompanyRepository.saveAndFlush(insuranceCompany);
        insuranceCompanySearchRepository.save(insuranceCompany);

        // Search the insuranceCompany
        restInsuranceCompanyMockMvc.perform(get("/api/_search/insurance-companies?query=id:" + insuranceCompany.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceCompany.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].address_1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
            .andExpect(jsonPath("$.[*].address_2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
            .andExpect(jsonPath("$.[*].address_3").value(hasItem(DEFAULT_ADDRESS_3.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceCompany.class);
        InsuranceCompany insuranceCompany1 = new InsuranceCompany();
        insuranceCompany1.setId(1L);
        InsuranceCompany insuranceCompany2 = new InsuranceCompany();
        insuranceCompany2.setId(insuranceCompany1.getId());
        assertThat(insuranceCompany1).isEqualTo(insuranceCompany2);
        insuranceCompany2.setId(2L);
        assertThat(insuranceCompany1).isNotEqualTo(insuranceCompany2);
        insuranceCompany1.setId(null);
        assertThat(insuranceCompany1).isNotEqualTo(insuranceCompany2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceCompanyDTO.class);
        InsuranceCompanyDTO insuranceCompanyDTO1 = new InsuranceCompanyDTO();
        insuranceCompanyDTO1.setId(1L);
        InsuranceCompanyDTO insuranceCompanyDTO2 = new InsuranceCompanyDTO();
        assertThat(insuranceCompanyDTO1).isNotEqualTo(insuranceCompanyDTO2);
        insuranceCompanyDTO2.setId(insuranceCompanyDTO1.getId());
        assertThat(insuranceCompanyDTO1).isEqualTo(insuranceCompanyDTO2);
        insuranceCompanyDTO2.setId(2L);
        assertThat(insuranceCompanyDTO1).isNotEqualTo(insuranceCompanyDTO2);
        insuranceCompanyDTO1.setId(null);
        assertThat(insuranceCompanyDTO1).isNotEqualTo(insuranceCompanyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(insuranceCompanyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(insuranceCompanyMapper.fromId(null)).isNull();
    }
}
