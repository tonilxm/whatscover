package com.whatscover.service.impl;

import com.whatscover.domain.InsuranceCompany;
import com.whatscover.repository.InsuranceCompanyRepository;
import com.whatscover.repository.search.InsuranceCompanySearchRepository;
import com.whatscover.service.InsuranceCompanyService;
import com.whatscover.service.dto.InsuranceCompanyDTO;
import com.whatscover.service.mapper.InsuranceCompanyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing InsuranceCompany.
 */
@Service
@Transactional
public class InsuranceCompanyServiceImpl implements InsuranceCompanyService{

    private final Logger log = LoggerFactory.getLogger(InsuranceCompanyServiceImpl.class);

    private final InsuranceCompanyRepository insuranceCompanyRepository;

    private final InsuranceCompanyMapper insuranceCompanyMapper;

    private final InsuranceCompanySearchRepository insuranceCompanySearchRepository;

    public InsuranceCompanyServiceImpl(InsuranceCompanyRepository insuranceCompanyRepository, InsuranceCompanyMapper insuranceCompanyMapper, InsuranceCompanySearchRepository insuranceCompanySearchRepository) {
        this.insuranceCompanyRepository = insuranceCompanyRepository;
        this.insuranceCompanyMapper = insuranceCompanyMapper;
        this.insuranceCompanySearchRepository = insuranceCompanySearchRepository;
    }

    /**
     * Save a insuranceCompany.
     *
     * @param insuranceCompanyDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public InsuranceCompanyDTO save(InsuranceCompanyDTO insuranceCompanyDTO) {
        log.debug("Request to save InsuranceCompany : {}", insuranceCompanyDTO);
        InsuranceCompany insuranceCompany = insuranceCompanyMapper.toEntity(insuranceCompanyDTO);
        insuranceCompany = insuranceCompanyRepository.save(insuranceCompany);
        InsuranceCompanyDTO result = insuranceCompanyMapper.toDto(insuranceCompany);
        insuranceCompanySearchRepository.save(insuranceCompany);
        return result;
    }

    /**
     *  Get all the insuranceCompanies.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InsuranceCompanyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InsuranceCompanies");
        return insuranceCompanyRepository.findAll(pageable)
            .map(insuranceCompanyMapper::toDto);
    }

    /**
     *  Get one insuranceCompany by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public InsuranceCompanyDTO findOne(Long id) {
        log.debug("Request to get InsuranceCompany : {}", id);
        InsuranceCompany insuranceCompany = insuranceCompanyRepository.findOne(id);
        return insuranceCompanyMapper.toDto(insuranceCompany);
    }

    /**
     *  Delete the  insuranceCompany by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete InsuranceCompany : {}", id);
        insuranceCompanyRepository.delete(id);
        insuranceCompanySearchRepository.delete(id);
    }

    /**
     * Search for the insuranceCompany by name.
     *
     *  @param name company insurance name to look for
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InsuranceCompanyDTO> search(String name, Pageable pageable) {
        log.debug("Request to search for a page of InsuranceCompanies for query {}", name);
        Page<InsuranceCompany> result = insuranceCompanySearchRepository.searchByName(name, pageable);
        return result.map(insuranceCompanyMapper::toDto);
    }
}
