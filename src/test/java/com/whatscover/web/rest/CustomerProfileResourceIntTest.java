package com.whatscover.web.rest;

import com.whatscover.WhatscoverApp;
import com.whatscover.domain.CustomerProfile;
import com.whatscover.domain.User;
import com.whatscover.domain.enumeration.Gender;
import com.whatscover.repository.CustomerProfileRepository;
import com.whatscover.repository.search.CustomerProfileSearchRepository;
import com.whatscover.service.CustomerProfileService;
import com.whatscover.service.dto.CustomerProfileDTO;
import com.whatscover.service.mapper.CustomerProfileMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Test class for the CustomerProfileResource REST controller.
 *
 * @see CustomerProfileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WhatscoverApp.class)
public class CustomerProfileResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private CustomerProfileRepository customerProfileRepository;

    @Autowired
    private CustomerProfileMapper customerProfileMapper;

    @Autowired
    private CustomerProfileService customerProfileService;

    @Autowired
    private CustomerProfileSearchRepository customerProfileSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCustomerProfileMockMvc;

    private CustomerProfile customerProfile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerProfileResource customerProfileResource = new CustomerProfileResource(customerProfileService);
        this.restCustomerProfileMockMvc = MockMvcBuilders.standaloneSetup(customerProfileResource)
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
    public static CustomerProfile createEntity(EntityManager em) {
        CustomerProfile customerProfile = new CustomerProfile()
            .firstName(DEFAULT_FIRST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .gender(DEFAULT_GENDER)
            .email(DEFAULT_EMAIL)
            .dob(DEFAULT_DOB);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        customerProfile.setUser(user);
        return customerProfile;
    }

    @Before
    public void initTest() {
        customerProfileSearchRepository.deleteAll();
        customerProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomerProfile() throws Exception {
        int databaseSizeBeforeCreate = customerProfileRepository.findAll().size();

        // Create the CustomerProfile
        CustomerProfileDTO customerProfileDTO = customerProfileMapper.toDto(customerProfile);
        restCustomerProfileMockMvc.perform(post("/api/customer-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerProfileDTO)))
            .andExpect(status().isCreated());

        // Validate the CustomerProfile in the database
        List<CustomerProfile> customerProfileList = customerProfileRepository.findAll();
        assertThat(customerProfileList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerProfile testCustomerProfile = customerProfileList.get(customerProfileList.size() - 1);
        assertThat(testCustomerProfile.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCustomerProfile.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testCustomerProfile.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCustomerProfile.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testCustomerProfile.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCustomerProfile.getDob()).isEqualTo(DEFAULT_DOB);

        // Validate the CustomerProfile in Elasticsearch
        CustomerProfile customerProfileEs = customerProfileSearchRepository.findOne(testCustomerProfile.getId());
        assertThat(customerProfileEs).isEqualToComparingFieldByField(testCustomerProfile);
    }

    @Test
    @Transactional
    public void createCustomerProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerProfileRepository.findAll().size();

        // Create the CustomerProfile with an existing ID
        customerProfile.setId(1L);
        CustomerProfileDTO customerProfileDTO = customerProfileMapper.toDto(customerProfile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerProfileMockMvc.perform(post("/api/customer-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CustomerProfile> customerProfileList = customerProfileRepository.findAll();
        assertThat(customerProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerProfileRepository.findAll().size();
        // set the field null
        customerProfile.setFirstName(null);

        // Create the CustomerProfile, which fails.
        CustomerProfileDTO customerProfileDTO = customerProfileMapper.toDto(customerProfile);

        restCustomerProfileMockMvc.perform(post("/api/customer-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerProfileDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerProfile> customerProfileList = customerProfileRepository.findAll();
        assertThat(customerProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomerProfiles() throws Exception {
        // Initialize the database
        customerProfileRepository.saveAndFlush(customerProfile);

        // Get all the customerProfileList
        restCustomerProfileMockMvc.perform(get("/api/customer-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())));
    }

    @Test
    @Transactional
    public void getCustomerProfile() throws Exception {
        // Initialize the database
        customerProfileRepository.saveAndFlush(customerProfile);

        // Get the customerProfile
        restCustomerProfileMockMvc.perform(get("/api/customer-profiles/{id}", customerProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customerProfile.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerProfile() throws Exception {
        // Get the customerProfile
        restCustomerProfileMockMvc.perform(get("/api/customer-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerProfile() throws Exception {
        // Initialize the database
        customerProfileRepository.saveAndFlush(customerProfile);
        customerProfileSearchRepository.save(customerProfile);
        int databaseSizeBeforeUpdate = customerProfileRepository.findAll().size();

        // Update the customerProfile
        CustomerProfile updatedCustomerProfile = customerProfileRepository.findOne(customerProfile.getId());
        updatedCustomerProfile
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .gender(UPDATED_GENDER)
            .email(UPDATED_EMAIL)
            .dob(UPDATED_DOB);
        CustomerProfileDTO customerProfileDTO = customerProfileMapper.toDto(updatedCustomerProfile);

        restCustomerProfileMockMvc.perform(put("/api/customer-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerProfileDTO)))
            .andExpect(status().isOk());

        // Validate the CustomerProfile in the database
        List<CustomerProfile> customerProfileList = customerProfileRepository.findAll();
        assertThat(customerProfileList).hasSize(databaseSizeBeforeUpdate);
        CustomerProfile testCustomerProfile = customerProfileList.get(customerProfileList.size() - 1);
        assertThat(testCustomerProfile.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCustomerProfile.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testCustomerProfile.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCustomerProfile.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testCustomerProfile.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCustomerProfile.getDob()).isEqualTo(UPDATED_DOB);

        // Validate the CustomerProfile in Elasticsearch
        CustomerProfile customerProfileEs = customerProfileSearchRepository.findOne(testCustomerProfile.getId());
        assertThat(customerProfileEs).isEqualToComparingFieldByField(testCustomerProfile);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomerProfile() throws Exception {
        int databaseSizeBeforeUpdate = customerProfileRepository.findAll().size();

        // Create the CustomerProfile
        CustomerProfileDTO customerProfileDTO = customerProfileMapper.toDto(customerProfile);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCustomerProfileMockMvc.perform(put("/api/customer-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerProfileDTO)))
            .andExpect(status().isCreated());

        // Validate the CustomerProfile in the database
        List<CustomerProfile> customerProfileList = customerProfileRepository.findAll();
        assertThat(customerProfileList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCustomerProfile() throws Exception {
        // Initialize the database
        customerProfileRepository.saveAndFlush(customerProfile);
        customerProfileSearchRepository.save(customerProfile);
        int databaseSizeBeforeDelete = customerProfileRepository.findAll().size();

        // Get the customerProfile
        restCustomerProfileMockMvc.perform(delete("/api/customer-profiles/{id}", customerProfile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean customerProfileExistsInEs = customerProfileSearchRepository.exists(customerProfile.getId());
        assertThat(customerProfileExistsInEs).isFalse();

        // Validate the database is empty
        List<CustomerProfile> customerProfileList = customerProfileRepository.findAll();
        assertThat(customerProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCustomerProfile() throws Exception {
        // Initialize the database
        customerProfileRepository.saveAndFlush(customerProfile);
        customerProfileSearchRepository.save(customerProfile);

        // Search the customerProfile
        restCustomerProfileMockMvc.perform(get("/api/_search/customer-profiles?query=id:" + customerProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerProfile.class);
        CustomerProfile customerProfile1 = new CustomerProfile();
        customerProfile1.setId(1L);
        CustomerProfile customerProfile2 = new CustomerProfile();
        customerProfile2.setId(customerProfile1.getId());
        assertThat(customerProfile1).isEqualTo(customerProfile2);
        customerProfile2.setId(2L);
        assertThat(customerProfile1).isNotEqualTo(customerProfile2);
        customerProfile1.setId(null);
        assertThat(customerProfile1).isNotEqualTo(customerProfile2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerProfileDTO.class);
        CustomerProfileDTO customerProfileDTO1 = new CustomerProfileDTO();
        customerProfileDTO1.setId(1L);
        CustomerProfileDTO customerProfileDTO2 = new CustomerProfileDTO();
        assertThat(customerProfileDTO1).isNotEqualTo(customerProfileDTO2);
        customerProfileDTO2.setId(customerProfileDTO1.getId());
        assertThat(customerProfileDTO1).isEqualTo(customerProfileDTO2);
        customerProfileDTO2.setId(2L);
        assertThat(customerProfileDTO1).isNotEqualTo(customerProfileDTO2);
        customerProfileDTO1.setId(null);
        assertThat(customerProfileDTO1).isNotEqualTo(customerProfileDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(customerProfileMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(customerProfileMapper.fromId(null)).isNull();
    }
}
