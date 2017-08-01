package com.whatscover.service.impl;

import com.whatscover.domain.CustomerProfile;
import com.whatscover.domain.User;
import com.whatscover.repository.CustomerProfileRepository;
import com.whatscover.repository.search.CustomerProfileSearchRepository;
import com.whatscover.service.CustomerProfileService;
import com.whatscover.service.UserService;
import com.whatscover.service.dto.CustomerProfileDTO;
import com.whatscover.service.exception.BusinessException;
import com.whatscover.service.mapper.CustomerProfileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing CustomerProfile.
 */
@Service
@Transactional
public class CustomerProfileServiceImpl implements CustomerProfileService{

    private final Logger log = LoggerFactory.getLogger(CustomerProfileServiceImpl.class);

    private final CustomerProfileRepository customerProfileRepository;

    private final CustomerProfileMapper customerProfileMapper;

    private final CustomerProfileSearchRepository customerProfileSearchRepository;

    @Autowired
    private UserService userService;

    public CustomerProfileServiceImpl(CustomerProfileRepository customerProfileRepository, CustomerProfileMapper customerProfileMapper, CustomerProfileSearchRepository customerProfileSearchRepository) {
        this.customerProfileRepository = customerProfileRepository;
        this.customerProfileMapper = customerProfileMapper;
        this.customerProfileSearchRepository = customerProfileSearchRepository;
    }

    /**
     * Create customer profile and user info and link them both as ROLE_USER
     * @param customerProfileDTO the entity to save
     * @return
     */
    @Override
    public CustomerProfileDTO create(CustomerProfileDTO customerProfileDTO, String randomPassword) throws BusinessException {
        log.debug("Request to save CustomerProfile : {}", customerProfileDTO);
        // check email not empty
        if (customerProfileDTO.getEmail().isEmpty()) new BusinessException("Email is required.");

        // check user with existing email
        boolean userExist = userService.checkUserExistByEmail(customerProfileDTO.getEmail());
        if (userExist) throw new BusinessException("User with email : " + customerProfileDTO.getEmail() + " already exist.");

        // create user
        User newUser = userService.createUser(customerProfileDTO.getFirstName(), customerProfileDTO.getLastName(), customerProfileDTO.getEmail(), randomPassword);

        // then customer profile
        CustomerProfile customerProfile = customerProfileMapper.toEntity(customerProfileDTO);
        customerProfile.setUser(newUser);
        customerProfile = customerProfileRepository.save(customerProfile);
        CustomerProfileDTO result = customerProfileMapper.toDto(customerProfile);
        customerProfileSearchRepository.save(customerProfile);

        return result;
    }

    /**
     * Save a customerProfile.
     *
     * @param customerProfileDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CustomerProfileDTO save(CustomerProfileDTO customerProfileDTO) {
        log.debug("Request to save CustomerProfile : {}", customerProfileDTO);
        CustomerProfile customerProfile = customerProfileMapper.toEntity(customerProfileDTO);
        customerProfile = customerProfileRepository.save(customerProfile);
        CustomerProfileDTO result = customerProfileMapper.toDto(customerProfile);
        customerProfileSearchRepository.save(customerProfile);
        return result;
    }

    /**
     *  Get all the customerProfiles.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomerProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerProfiles");
        return customerProfileRepository.findAll(pageable)
            .map(customerProfileMapper::toDto);
    }

    /**
     *  Get one customerProfile by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CustomerProfileDTO findOne(Long id) {
        log.debug("Request to get CustomerProfile : {}", id);
        CustomerProfile customerProfile = customerProfileRepository.findOne(id);
        return customerProfileMapper.toDto(customerProfile);
    }

    /**
     *  Delete customer profile, user and user authority.
     *  Using userService delete user function.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        CustomerProfile customerProfile = customerProfileRepository.findOne(id);
        userService.deleteUser(customerProfile.getUser().getLogin());
    }

    /**
     * Search for the customerProfile corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entitiess
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomerProfileDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CustomerProfiles for query {}", query);
        Page<CustomerProfile> result = customerProfileSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(customerProfileMapper::toDto);
    }

    @Override
    public void deleteByUserId(Long userId) {
        log.debug("Request to delete CustomerProfile by userId: {}", userId);
        customerProfileRepository.deleteByUserId(userId);
    }

    /**
     * Search for the customerProfile corresponding by name to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entitiess
     */
    @Override
    public Page<CustomerProfileDTO> searchByName(String query, Pageable pageable) {
        log.debug("Request to search for a page of CustomerProfiles by name for query {}", query);
        Page<CustomerProfile> result = customerProfileSearchRepository.searchByName(query, pageable);
        return result.map(customerProfileMapper::toDto);
    }
}
