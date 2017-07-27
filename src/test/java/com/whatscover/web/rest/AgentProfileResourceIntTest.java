package com.whatscover.web.rest;

import com.whatscover.WhatscoverApp;

import com.whatscover.domain.AgentProfile;
import com.whatscover.repository.AgentProfileRepository;
import com.whatscover.repository.search.AgentProfileSearchRepository;
import com.whatscover.service.AgentProfileService;
import com.whatscover.service.MailService;
import com.whatscover.service.UserService;
import com.whatscover.service.dto.AgentProfileDTO;
import com.whatscover.service.mapper.AgentProfileMapper;
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

import com.whatscover.domain.enumeration.Gender;
/**
 * Test class for the AgentProfileResource REST controller.
 *
 * @see AgentProfileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WhatscoverApp.class)
public class AgentProfileResourceIntTest {

    private static final String DEFAULT_AGENT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AGENT_CODE = "BBBBBBBBBB";

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
    private AgentProfileRepository agentProfileRepository;

    @Autowired 
    private AgentProfileService agentProfileService;
    
    @Autowired
    private AgentProfileMapper agentProfileMapper;

    @Autowired
    private AgentProfileSearchRepository agentProfileSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAgentProfileMockMvc;

    private AgentProfile agentProfile;
    
    private MailService mailService;

    private UserService userService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AgentProfileResource agentProfileResource = new AgentProfileResource(agentProfileService, agentProfileRepository, mailService, userService);
        this.restAgentProfileMockMvc = MockMvcBuilders.standaloneSetup(agentProfileResource)
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
    public static AgentProfile createEntity(EntityManager em) {
        AgentProfile agentProfile = new AgentProfile()
            .agent_code(DEFAULT_AGENT_CODE)
            .first_name(DEFAULT_FIRST_NAME)
            .middle_name(DEFAULT_MIDDLE_NAME)
            .last_name(DEFAULT_LAST_NAME)
            .gender(DEFAULT_GENDER)
            .email(DEFAULT_EMAIL)
            .dob(DEFAULT_DOB);
        return agentProfile;
    }

    @Before
    public void initTest() {
        agentProfileSearchRepository.deleteAll();
        agentProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createAgentProfile() throws Exception {
        int databaseSizeBeforeCreate = agentProfileRepository.findAll().size();

        // Create the AgentProfile
        AgentProfileDTO agentProfileDTO = agentProfileMapper.toDto(agentProfile);
        restAgentProfileMockMvc.perform(post("/api/agent-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentProfileDTO)))
            .andExpect(status().isCreated());

        // Validate the AgentProfile in the database
        List<AgentProfile> agentProfileList = agentProfileRepository.findAll();
        assertThat(agentProfileList).hasSize(databaseSizeBeforeCreate + 1);
        AgentProfile testAgentProfile = agentProfileList.get(agentProfileList.size() - 1);
        assertThat(testAgentProfile.getAgent_code()).isEqualTo(DEFAULT_AGENT_CODE);
        assertThat(testAgentProfile.getFirst_name()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAgentProfile.getMiddle_name()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testAgentProfile.getLast_name()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAgentProfile.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testAgentProfile.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAgentProfile.getDob()).isEqualTo(DEFAULT_DOB);

        // Validate the AgentProfile in Elasticsearch
        AgentProfile agentProfileEs = agentProfileSearchRepository.findOne(testAgentProfile.getId());
        assertThat(agentProfileEs.equals(testAgentProfile));
    }

    @Test
    @Transactional
    public void createAgentProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = agentProfileRepository.findAll().size();

        // Create the AgentProfile with an existing ID
        agentProfile.setId(1L);
        AgentProfileDTO agentProfileDTO = agentProfileMapper.toDto(agentProfile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgentProfileMockMvc.perform(post("/api/agent-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AgentProfile> agentProfileList = agentProfileRepository.findAll();
        assertThat(agentProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAgent_codeIsRequired() throws Exception {
        int databaseSizeBeforeTest = agentProfileRepository.findAll().size();
        // set the field null
        agentProfile.setAgent_code(null);

        // Create the AgentProfile, which fails.
        AgentProfileDTO agentProfileDTO = agentProfileMapper.toDto(agentProfile);

        restAgentProfileMockMvc.perform(post("/api/agent-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentProfileDTO)))
            .andExpect(status().isBadRequest());

        List<AgentProfile> agentProfileList = agentProfileRepository.findAll();
        assertThat(agentProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = agentProfileRepository.findAll().size();
        // set the field null
        agentProfile.setEmail(null);

        // Create the AgentProfile, which fails.
        AgentProfileDTO agentProfileDTO = agentProfileMapper.toDto(agentProfile);

        restAgentProfileMockMvc.perform(post("/api/agent-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentProfileDTO)))
            .andExpect(status().isBadRequest());

        List<AgentProfile> agentProfileList = agentProfileRepository.findAll();
        assertThat(agentProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAgentProfiles() throws Exception {
        // Initialize the database
        agentProfileRepository.saveAndFlush(agentProfile);

        // Get all the agentProfileList
        restAgentProfileMockMvc.perform(get("/api/agent-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agentProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].agent_code").value(hasItem(DEFAULT_AGENT_CODE.toString())))
            .andExpect(jsonPath("$.[*].first_name").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].middle_name").value(hasItem(DEFAULT_MIDDLE_NAME.toString())))
            .andExpect(jsonPath("$.[*].last_name").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())));
    }

    @Test
    @Transactional
    public void getAgentProfile() throws Exception {
        // Initialize the database
        agentProfileRepository.saveAndFlush(agentProfile);

        // Get the agentProfile
        restAgentProfileMockMvc.perform(get("/api/agent-profiles/{id}", agentProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(agentProfile.getId().intValue()))
            .andExpect(jsonPath("$.agent_code").value(DEFAULT_AGENT_CODE.toString()))
            .andExpect(jsonPath("$.first_name").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.middle_name").value(DEFAULT_MIDDLE_NAME.toString()))
            .andExpect(jsonPath("$.last_name").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAgentProfile() throws Exception {
        // Get the agentProfile
        restAgentProfileMockMvc.perform(get("/api/agent-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgentProfile() throws Exception {
        // Initialize the database
        agentProfileRepository.saveAndFlush(agentProfile);
        agentProfileSearchRepository.save(agentProfile);
        int databaseSizeBeforeUpdate = agentProfileRepository.findAll().size();

        // Update the agentProfile
        AgentProfile updatedAgentProfile = agentProfileRepository.findOne(agentProfile.getId());
        updatedAgentProfile
            .agent_code(UPDATED_AGENT_CODE)
            .first_name(UPDATED_FIRST_NAME)
            .middle_name(UPDATED_MIDDLE_NAME)
            .last_name(UPDATED_LAST_NAME)
            .gender(UPDATED_GENDER)
            .email(UPDATED_EMAIL)
            .dob(UPDATED_DOB);
        AgentProfileDTO agentProfileDTO = agentProfileMapper.toDto(updatedAgentProfile);

        restAgentProfileMockMvc.perform(put("/api/agent-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentProfileDTO)))
            .andExpect(status().isOk());

        // Validate the AgentProfile in the database
        List<AgentProfile> agentProfileList = agentProfileRepository.findAll();
        assertThat(agentProfileList).hasSize(databaseSizeBeforeUpdate);
        AgentProfile testAgentProfile = agentProfileList.get(agentProfileList.size() - 1);
        assertThat(testAgentProfile.getAgent_code()).isEqualTo(UPDATED_AGENT_CODE);
        assertThat(testAgentProfile.getFirst_name()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAgentProfile.getMiddle_name()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testAgentProfile.getLast_name()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAgentProfile.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testAgentProfile.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAgentProfile.getDob()).isEqualTo(UPDATED_DOB);

        // Validate the AgentProfile in Elasticsearch
        AgentProfile agentProfileEs = agentProfileSearchRepository.findOne(testAgentProfile.getId());
        assertThat(agentProfileEs.equals(testAgentProfile));
    }

    @Test
    @Transactional
    public void updateNonExistingAgentProfile() throws Exception {
        int databaseSizeBeforeUpdate = agentProfileRepository.findAll().size();

        // Create the AgentProfile
        AgentProfileDTO agentProfileDTO = agentProfileMapper.toDto(agentProfile);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAgentProfileMockMvc.perform(put("/api/agent-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentProfileDTO)))
            .andExpect(status().isCreated());

        // Validate the AgentProfile in the database
        List<AgentProfile> agentProfileList = agentProfileRepository.findAll();
        assertThat(agentProfileList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAgentProfile() throws Exception {
        // Initialize the database
        agentProfileRepository.saveAndFlush(agentProfile);
        agentProfileSearchRepository.save(agentProfile);
        int databaseSizeBeforeDelete = agentProfileRepository.findAll().size();

        // Get the agentProfile
        restAgentProfileMockMvc.perform(delete("/api/agent-profiles/{id}", agentProfile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean agentProfileExistsInEs = agentProfileSearchRepository.exists(agentProfile.getId());
        assertThat(agentProfileExistsInEs).isFalse();

        // Validate the database is empty
        List<AgentProfile> agentProfileList = agentProfileRepository.findAll();
        assertThat(agentProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAgentProfile() throws Exception {
        // Initialize the database
        agentProfileRepository.saveAndFlush(agentProfile);
        agentProfileSearchRepository.save(agentProfile);

        // Search the agentProfile
        restAgentProfileMockMvc.perform(get("/api/_search/agent-profiles?query=id:" + agentProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agentProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].agent_code").value(hasItem(DEFAULT_AGENT_CODE.toString())))
            .andExpect(jsonPath("$.[*].first_name").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].middle_name").value(hasItem(DEFAULT_MIDDLE_NAME.toString())))
            .andExpect(jsonPath("$.[*].last_name").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentProfile.class);
        AgentProfile agentProfile1 = new AgentProfile();
        agentProfile1.setId(1L);
        AgentProfile agentProfile2 = new AgentProfile();
        agentProfile2.setId(agentProfile1.getId());
        assertThat(agentProfile1).isEqualTo(agentProfile2);
        agentProfile2.setId(2L);
        assertThat(agentProfile1).isNotEqualTo(agentProfile2);
        agentProfile1.setId(null);
        assertThat(agentProfile1).isNotEqualTo(agentProfile2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentProfileDTO.class);
        AgentProfileDTO agentProfileDTO1 = new AgentProfileDTO();
        agentProfileDTO1.setId(1L);
        AgentProfileDTO agentProfileDTO2 = new AgentProfileDTO();
        assertThat(agentProfileDTO1).isNotEqualTo(agentProfileDTO2);
        agentProfileDTO2.setId(agentProfileDTO1.getId());
        assertThat(agentProfileDTO1).isEqualTo(agentProfileDTO2);
        agentProfileDTO2.setId(2L);
        assertThat(agentProfileDTO1).isNotEqualTo(agentProfileDTO2);
        agentProfileDTO1.setId(null);
        assertThat(agentProfileDTO1).isNotEqualTo(agentProfileDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(agentProfileMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(agentProfileMapper.fromId(null)).isNull();
    }
}
