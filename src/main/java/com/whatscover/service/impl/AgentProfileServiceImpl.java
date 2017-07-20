package com.whatscover.service.impl;

import com.whatscover.service.AgentProfileService;
import com.whatscover.config.Constants;
import com.whatscover.domain.AgentProfile;
import com.whatscover.repository.AgentProfileRepository;
import com.whatscover.repository.search.AgentProfileSearchRepository;
import com.whatscover.service.dto.AgentProfileDTO;
import com.whatscover.service.mapper.AgentProfileMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.elasticsearch.index.query.QueryBuilders.*;

import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;

/**
 * Service Implementation for managing InsuranceCompany.
 */
@Service
@Transactional
public class AgentProfileServiceImpl implements AgentProfileService{

    private final Logger log = LoggerFactory.getLogger(AgentProfileServiceImpl.class);

    private final AgentProfileRepository agentProfileRepository;

    private final AgentProfileMapper agentProfileMapper;
    
    private final AgentProfileSearchRepository agentProfileSearchRepository;

    public AgentProfileServiceImpl(AgentProfileRepository agentProfileRepository, AgentProfileMapper agentProfileMapper, 
    		AgentProfileSearchRepository agentProfileSearchRepository) {
        this.agentProfileRepository = agentProfileRepository;
        this.agentProfileMapper = agentProfileMapper;
        this.agentProfileSearchRepository = agentProfileSearchRepository;
    }

    @Override
	public AgentProfileDTO save(AgentProfileDTO agentProfileDTO) {
    	log.debug("Request to save AgentProfile : {}", agentProfileDTO);
    	AgentProfile agentProfile = agentProfileMapper.toEntity(agentProfileDTO);
    	agentProfile = agentProfileRepository.save(agentProfile);
    	AgentProfileDTO result = agentProfileMapper.toDto(agentProfile);
    	agentProfileSearchRepository.save(agentProfile);
		return result;
	}

	@Override
    @Transactional(readOnly = true)
	public AgentProfileDTO findOne(Long id) {
        log.debug("Request to get AgentProfile : {}", id);
        AgentProfile agentProfile = agentProfileRepository.findOne(id);
        return agentProfileMapper.toDto(agentProfile);
	}

	@Override
	public Page<AgentProfileDTO> findAll(Pageable pageable) {
		log.debug("Request to get all AgentProfiles");
        return agentProfileRepository.findAll(pageable)
            .map(agentProfileMapper::toDto);
	}

	@Override
	public void delete(Long id) {
        log.debug("Request to get AgentProfile : {}", id);
        AgentProfile agentProfile = agentProfileRepository.findOne(id);
        agentProfileMapper.toDto(agentProfile);
	}

	@Override
    @Transactional(readOnly = true)
	public Page<AgentProfileDTO> search(String []queryData, Pageable pageable) {
		log.debug("Request to search for a page of AgentProfiles for query {}", 
				queryData.toString());
		// Elastic boolQuery to search multiple file
		Page<AgentProfile> result = agentProfileSearchRepository.search(boolQuery()
        		.should(queryStringQuery(queryData[0]).defaultField(        				
        				Constants.AGENT_PROFILE_FIRST_NAME))
        		.should(queryStringQuery(queryData[1]).defaultField(
        				Constants.AGENT_PROFILE_MIDDLE_NAME)
        				.defaultOperator(Operator.AND))
				.should(queryStringQuery(queryData[2]).defaultField(
        				Constants.AGENT_PROFILE_LAST_NAME)
        				.defaultOperator(Operator.AND))
				.should(queryStringQuery(queryData[3]).defaultField(
        				Constants.AGENT_PROFILE_INSURANCE_COMPANY)
        				.defaultOperator(Operator.AND))
				.should(queryStringQuery(queryData[4]).defaultField(
        				Constants.AGENT_PROFILE_INSURANCE_AGENCY)
        				.defaultOperator(Operator.AND)),
        		pageable);
        return result.map(agentProfileMapper::toDto);
	}
}
