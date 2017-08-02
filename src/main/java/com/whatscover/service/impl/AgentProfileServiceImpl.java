package com.whatscover.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.Optional;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.whatscover.config.Constants;
import com.whatscover.domain.AgentProfile;
import com.whatscover.repository.AgentProfileRepository;
import com.whatscover.repository.search.AgentProfileSearchRepository;
import com.whatscover.service.AgentProfileService;
import com.whatscover.service.dto.AgentProfileDTO;
import com.whatscover.service.mapper.AgentProfileMapper;

/**
 * Service Implementation for managing AgentProfile.
 */
@Service
@Transactional
public class AgentProfileServiceImpl implements AgentProfileService{

    private final Logger log = LoggerFactory.getLogger(AgentProfileServiceImpl.class);

    private final AgentProfileRepository agentProfileRepository;

    private final AgentProfileMapper agentProfileMapper;

    private final AgentProfileSearchRepository agentProfileSearchRepository;

    public AgentProfileServiceImpl(AgentProfileRepository agentProfileRepository, AgentProfileMapper agentProfileMapper, AgentProfileSearchRepository agentProfileSearchRepository) {
        this.agentProfileRepository = agentProfileRepository;
        this.agentProfileMapper = agentProfileMapper;
        this.agentProfileSearchRepository = agentProfileSearchRepository;
    }

    /**
     * Save a agentProfile.
     *
     * @param agentProfileDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AgentProfileDTO save(AgentProfileDTO agentProfileDTO) {
        log.debug("Request to save AgentProfile : {}", agentProfileDTO);
        AgentProfile agentProfile = agentProfileMapper.toEntity(agentProfileDTO);
        agentProfile = agentProfileRepository.save(agentProfile);
        AgentProfileDTO result = agentProfileMapper.toDto(agentProfile);
        agentProfileSearchRepository.save(agentProfile);
        return result;
    }

    /**
     *  Get all the agentProfiles.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AgentProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AgentProfiles");
        return agentProfileRepository.findAll(pageable)
            .map(agentProfileMapper::toDto);
    }

    /**
     *  Get one agentProfile by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AgentProfileDTO findOne(Long id) {
        log.debug("Request to get AgentProfile : {}", id);
        AgentProfile agentProfile = agentProfileRepository.findOne(id);
        return agentProfileMapper.toDto(agentProfile);
    }

    /**
     *  Delete the  agentProfile by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AgentProfile : {}", id);
        agentProfileRepository.delete(id);
        agentProfileSearchRepository.delete(id);
    }

    /**
     *  Get one agentProfile by email.
     *
     *  @param email the email of the entity
     *  @return the entity
     */
	@Override
	public AgentProfileDTO findOneByEmail(String email) {
        log.debug("Request to get AgentProfile : {}", email);
        Optional<AgentProfile> optAgentProfile = agentProfileRepository.findOneByEmail(email);
        AgentProfile agentProfile = new AgentProfile();
        if(optAgentProfile.isPresent()) {
            agentProfile = optAgentProfile.get();
        }
        return agentProfileMapper.toDto(agentProfile);
	}

    /**
     * Search for the agentProfile corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
	public Page<AgentProfileDTO> search(String []queryData, Pageable pageable) {
		log.debug("Request to search for a page of AgentProfiles for query {}", 
				queryData.toString());
		
		Page<AgentProfile> result = handleMultipleSearch(queryData, pageable);
		
        return result.map(agentProfileMapper::toDto);
	}
	
	/**
	 * Elastic boolQuery to search multiple file
	 * @param queryData
	 * @param pageable
	 * @return
	 */
	private Page<AgentProfile> handleMultipleSearch(String [] queryData, Pageable pageable) {

		Page<AgentProfile> result = null;
		BoolQueryBuilder queryBuilders = boolQuery();
		
		if (!queryData[0].equals("")) {
			queryBuilders.must(queryStringQuery(queryData[0])
    				.defaultField(Constants.AGENT_PROFILE_FIRST_NAME));
		}
		
		if (!queryData[1].equals("")) {
			queryBuilders.must(queryStringQuery(queryData[1])
    				.defaultField(Constants.AGENT_PROFILE_MIDDLE_NAME)
    				.defaultOperator(Operator.AND));
		}

		if (!queryData[2].equals("")) {
			queryBuilders.must(queryStringQuery(queryData[2])
					.defaultField(Constants.AGENT_PROFILE_LAST_NAME)
    				.defaultOperator(Operator.AND));
		}
		
		if (!queryData[3].equals("")) {
			queryBuilders.must(queryStringQuery(queryData[3])
					.defaultField(Constants.AGENT_PROFILE_INSURANCE_COMPANY)
    				.defaultOperator(Operator.AND));
		}
		
		if (!queryData[4].equals("")) {
			queryBuilders.must(queryStringQuery(queryData[4])
					.defaultField(Constants.AGENT_PROFILE_INSURANCE_AGENCY)
    				.defaultOperator(Operator.AND));
		}
		
		result = agentProfileSearchRepository.search(queryBuilders, pageable);
		
		return result;
	}

    /**
     * Search for the customerProfile corresponding by name to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entitiess
     */
    @Override
    public Page<AgentProfileDTO> searchByName(String [] queryData, Pageable pageable) {
        log.debug("Request to search for a page of CustomerProfiles by name for query {}", queryData);
        Page<AgentProfile> result = agentProfileSearchRepository.searchByName(queryData, pageable);
        return result.map(agentProfileMapper::toDto);
    }

}
