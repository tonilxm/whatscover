package com.whatscover.web.rest;

import com.whatscover.WhatscoverApp;
import com.whatscover.domain.Appointment;
import com.whatscover.domain.CustomerProfile;
import com.whatscover.domain.enumeration.AppointmentStatus;
import com.whatscover.repository.AppointmentRepository;
import com.whatscover.repository.search.AppointmentSearchRepository;
import com.whatscover.service.AppointmentService;
import com.whatscover.service.dto.AppointmentDTO;
import com.whatscover.service.mapper.AppointmentMapper;
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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.whatscover.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Test class for the AppointmentResource REST controller.
 *
 * @see AppointmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WhatscoverApp.class)
public class AppointmentResourceIntTest {

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final Float DEFAULT_LOCATION_GEO_LONG = 1F;
    private static final Float UPDATED_LOCATION_GEO_LONG = 2F;

    private static final Float DEFAULT_LOCATION_GEO_LAT = 1F;
    private static final Float UPDATED_LOCATION_GEO_LAT = 2F;

    private static final String DEFAULT_LOCATION_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_ADDRESS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATETIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Instant DEFAULT_ASSIGNED_DATETIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ASSIGNED_DATETIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final AppointmentStatus DEFAULT_STATUS = AppointmentStatus.NEW_APPOINTMENT;
    private static final AppointmentStatus UPDATED_STATUS = AppointmentStatus.WAITING_RESPONSE;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentSearchRepository appointmentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAppointmentMockMvc;

    private Appointment appointment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AppointmentResource appointmentResource = new AppointmentResource(appointmentService);
        this.restAppointmentMockMvc = MockMvcBuilders.standaloneSetup(appointmentResource)
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
    public static Appointment createEntity(EntityManager em) {
        Appointment appointment = new Appointment()
            .phone(DEFAULT_PHONE)
            .locationGeoLong(DEFAULT_LOCATION_GEO_LONG)
            .locationGeoLat(DEFAULT_LOCATION_GEO_LAT)
            .locationAddress(DEFAULT_LOCATION_ADDRESS)
            .datetime(DEFAULT_DATETIME)
            .assignedDatetime(DEFAULT_ASSIGNED_DATETIME)
            .status(DEFAULT_STATUS);
        // Add required entity
        CustomerProfile customerProfile = CustomerProfileResourceIntTest.createEntity(em);
        em.persist(customerProfile);
        em.flush();
        appointment.setCustomerProfile(customerProfile);
        return appointment;
    }

    @Before
    public void initTest() {
        appointmentSearchRepository.deleteAll();
        appointment = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppointment() throws Exception {
        int databaseSizeBeforeCreate = appointmentRepository.findAll().size();

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);
        restAppointmentMockMvc.perform(post("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeCreate + 1);
        Appointment testAppointment = appointmentList.get(appointmentList.size() - 1);
        assertThat(testAppointment.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testAppointment.getLocationGeoLong()).isEqualTo(DEFAULT_LOCATION_GEO_LONG);
        assertThat(testAppointment.getLocationGeoLat()).isEqualTo(DEFAULT_LOCATION_GEO_LAT);
        assertThat(testAppointment.getLocationAddress()).isEqualTo(DEFAULT_LOCATION_ADDRESS);
        assertThat(testAppointment.getDatetime()).isEqualTo(DEFAULT_DATETIME);
        assertThat(testAppointment.getAssignedDatetime()).isEqualTo(DEFAULT_ASSIGNED_DATETIME);
        assertThat(testAppointment.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Appointment in Elasticsearch
        Appointment appointmentEs = appointmentSearchRepository.findOne(testAppointment.getId());
        assertThat(appointmentEs).isEqualToComparingFieldByField(testAppointment);
    }

    @Test
    @Transactional
    public void createAppointmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appointmentRepository.findAll().size();

        // Create the Appointment with an existing ID
        appointment.setId(1L);
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppointmentMockMvc.perform(post("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointmentRepository.findAll().size();
        // set the field null
        appointment.setPhone(null);

        // Create the Appointment, which fails.
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        restAppointmentMockMvc.perform(post("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointmentRepository.findAll().size();
        // set the field null
        appointment.setLocationAddress(null);

        // Create the Appointment, which fails.
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        restAppointmentMockMvc.perform(post("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDatetimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointmentRepository.findAll().size();
        // set the field null
        appointment.setDatetime(null);

        // Create the Appointment, which fails.
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        restAppointmentMockMvc.perform(post("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointmentRepository.findAll().size();
        // set the field null
        appointment.setStatus(null);

        // Create the Appointment, which fails.
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        restAppointmentMockMvc.perform(post("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAppointments() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList
        restAppointmentMockMvc.perform(get("/api/appointments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointment.getId().intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].locationGeoLong").value(hasItem(DEFAULT_LOCATION_GEO_LONG.doubleValue())))
            .andExpect(jsonPath("$.[*].locationGeoLat").value(hasItem(DEFAULT_LOCATION_GEO_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].locationAddress").value(hasItem(DEFAULT_LOCATION_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].datetime").value(hasItem(sameInstant(DEFAULT_DATETIME))))
            .andExpect(jsonPath("$.[*].assignedDatetime").value(hasItem(DEFAULT_ASSIGNED_DATETIME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get the appointment
        restAppointmentMockMvc.perform(get("/api/appointments/{id}", appointment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(appointment.getId().intValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.locationGeoLong").value(DEFAULT_LOCATION_GEO_LONG.doubleValue()))
            .andExpect(jsonPath("$.locationGeoLat").value(DEFAULT_LOCATION_GEO_LAT.doubleValue()))
            .andExpect(jsonPath("$.locationAddress").value(DEFAULT_LOCATION_ADDRESS.toString()))
            .andExpect(jsonPath("$.datetime").value(sameInstant(DEFAULT_DATETIME)))
            .andExpect(jsonPath("$.assignedDatetime").value(DEFAULT_ASSIGNED_DATETIME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAppointment() throws Exception {
        // Get the appointment
        restAppointmentMockMvc.perform(get("/api/appointments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);
        appointmentSearchRepository.save(appointment);
        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();

        // Update the appointment
        Appointment updatedAppointment = appointmentRepository.findOne(appointment.getId());
        updatedAppointment
            .phone(UPDATED_PHONE)
            .locationGeoLong(UPDATED_LOCATION_GEO_LONG)
            .locationGeoLat(UPDATED_LOCATION_GEO_LAT)
            .locationAddress(UPDATED_LOCATION_ADDRESS)
            .datetime(UPDATED_DATETIME)
            .assignedDatetime(UPDATED_ASSIGNED_DATETIME)
            .status(UPDATED_STATUS);
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(updatedAppointment);

        restAppointmentMockMvc.perform(put("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isOk());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);
        Appointment testAppointment = appointmentList.get(appointmentList.size() - 1);
        assertThat(testAppointment.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testAppointment.getLocationGeoLong()).isEqualTo(UPDATED_LOCATION_GEO_LONG);
        assertThat(testAppointment.getLocationGeoLat()).isEqualTo(UPDATED_LOCATION_GEO_LAT);
        assertThat(testAppointment.getLocationAddress()).isEqualTo(UPDATED_LOCATION_ADDRESS);
        assertThat(testAppointment.getDatetime()).isEqualTo(UPDATED_DATETIME);
        assertThat(testAppointment.getAssignedDatetime()).isEqualTo(UPDATED_ASSIGNED_DATETIME);
        assertThat(testAppointment.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Appointment in Elasticsearch
        Appointment appointmentEs = appointmentSearchRepository.findOne(testAppointment.getId());
        assertThat(appointmentEs).isEqualToComparingFieldByField(testAppointment);
    }

    @Test
    @Transactional
    public void updateNonExistingAppointment() throws Exception {
        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAppointmentMockMvc.perform(put("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);
        appointmentSearchRepository.save(appointment);
        int databaseSizeBeforeDelete = appointmentRepository.findAll().size();

        // Get the appointment
        restAppointmentMockMvc.perform(delete("/api/appointments/{id}", appointment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean appointmentExistsInEs = appointmentSearchRepository.exists(appointment.getId());
        assertThat(appointmentExistsInEs).isFalse();

        // Validate the database is empty
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);
        appointmentSearchRepository.save(appointment);

        // Search the appointment
        restAppointmentMockMvc.perform(get("/api/_search/appointments?query=id:" + appointment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointment.getId().intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].locationGeoLong").value(hasItem(DEFAULT_LOCATION_GEO_LONG.doubleValue())))
            .andExpect(jsonPath("$.[*].locationGeoLat").value(hasItem(DEFAULT_LOCATION_GEO_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].locationAddress").value(hasItem(DEFAULT_LOCATION_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].datetime").value(hasItem(sameInstant(DEFAULT_DATETIME))))
            .andExpect(jsonPath("$.[*].assignedDatetime").value(hasItem(DEFAULT_ASSIGNED_DATETIME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appointment.class);
        Appointment appointment1 = new Appointment();
        appointment1.setId(1L);
        Appointment appointment2 = new Appointment();
        appointment2.setId(appointment1.getId());
        assertThat(appointment1).isEqualTo(appointment2);
        appointment2.setId(2L);
        assertThat(appointment1).isNotEqualTo(appointment2);
        appointment1.setId(null);
        assertThat(appointment1).isNotEqualTo(appointment2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppointmentDTO.class);
        AppointmentDTO appointmentDTO1 = new AppointmentDTO();
        appointmentDTO1.setId(1L);
        AppointmentDTO appointmentDTO2 = new AppointmentDTO();
        assertThat(appointmentDTO1).isNotEqualTo(appointmentDTO2);
        appointmentDTO2.setId(appointmentDTO1.getId());
        assertThat(appointmentDTO1).isEqualTo(appointmentDTO2);
        appointmentDTO2.setId(2L);
        assertThat(appointmentDTO1).isNotEqualTo(appointmentDTO2);
        appointmentDTO1.setId(null);
        assertThat(appointmentDTO1).isNotEqualTo(appointmentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(appointmentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(appointmentMapper.fromId(null)).isNull();
    }
}
