package com.whatscover.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.whatscover.domain.AgentProfile;

import com.whatscover.repository.AgentProfileRepository;
import com.whatscover.repository.search.AgentProfileSearchRepository;
import com.whatscover.web.rest.util.HeaderUtil;
import com.whatscover.web.rest.util.PaginationUtil;
import com.whatscover.service.dto.AgentProfileDTO;
import com.whatscover.service.mapper.AgentProfileMapper;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing AgentProfile.
 */
@RestController
@RequestMapping("/api")
public class AgentProfileResource {

    private final Logger log = LoggerFactory.getLogger(AgentProfileResource.class);

    private static final String ENTITY_NAME = "agentProfile";

    private final AgentProfileRepository agentProfileRepository;

    private final AgentProfileMapper agentProfileMapper;

    private final AgentProfileSearchRepository agentProfileSearchRepository;

    public AgentProfileResource(AgentProfileRepository agentProfileRepository, AgentProfileMapper agentProfileMapper, AgentProfileSearchRepository agentProfileSearchRepository) {
        this.agentProfileRepository = agentProfileRepository;
        this.agentProfileMapper = agentProfileMapper;
        this.agentProfileSearchRepository = agentProfileSearchRepository;
    }

    /**
     * POST  /agent-profiles : Create a new agentProfile.
     *
     * @param agentProfileDTO the agentProfileDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new agentProfileDTO, or with status 400 (Bad Request) if the agentProfile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/agent-profiles")
    @Timed
    public ResponseEntity<AgentProfileDTO> createAgentProfile(@Valid @RequestBody AgentProfileDTO agentProfileDTO) throws URISyntaxException {
        log.debug("REST request to save AgentProfile : {}", agentProfileDTO);
        if (agentProfileDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new agentProfile cannot already have an ID")).body(null);
        }
        AgentProfile agentProfile = agentProfileMapper.toEntity(agentProfileDTO);
        agentProfile = agentProfileRepository.save(agentProfile);
        AgentProfileDTO result = agentProfileMapper.toDto(agentProfile);
        agentProfileSearchRepository.save(agentProfile);
        return ResponseEntity.created(new URI("/api/agent-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /agent-profiles : Updates an existing agentProfile.
     *
     * @param agentProfileDTO the agentProfileDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated agentProfileDTO,
     * or with status 400 (Bad Request) if the agentProfileDTO is not valid,
     * or with status 500 (Internal Server Error) if the agentProfileDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/agent-profiles")
    @Timed
    public ResponseEntity<AgentProfileDTO> updateAgentProfile(@Valid @RequestBody AgentProfileDTO agentProfileDTO) throws URISyntaxException {
        log.debug("REST request to update AgentProfile : {}", agentProfileDTO);
        if (agentProfileDTO.getId() == null) {
            return createAgentProfile(agentProfileDTO);
        }
        AgentProfile agentProfile = agentProfileMapper.toEntity(agentProfileDTO);
        agentProfile = agentProfileRepository.save(agentProfile);
        AgentProfileDTO result = agentProfileMapper.toDto(agentProfile);
        agentProfileSearchRepository.save(agentProfile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, agentProfileDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /agent-profiles : get all the agentProfiles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of agentProfiles in body
     */
    @GetMapping("/agent-profiles")
    @Timed
    public ResponseEntity<List<AgentProfileDTO>> getAllAgentProfiles(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of AgentProfiles");
        Page<AgentProfile> page = agentProfileRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/agent-profiles");
        return new ResponseEntity<>(agentProfileMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /agent-profiles/:id : get the "id" agentProfile.
     *
     * @param id the id of the agentProfileDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the agentProfileDTO, or with status 404 (Not Found)
     */
    @GetMapping("/agent-profiles/{id}")
    @Timed
    public ResponseEntity<AgentProfileDTO> getAgentProfile(@PathVariable Long id) {
        log.debug("REST request to get AgentProfile : {}", id);
        AgentProfile agentProfile = agentProfileRepository.findOne(id);
        AgentProfileDTO agentProfileDTO = agentProfileMapper.toDto(agentProfile);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(agentProfileDTO));
    }

    /**
     * DELETE  /agent-profiles/:id : delete the "id" agentProfile.
     *
     * @param id the id of the agentProfileDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/agent-profiles/{id}")
    @Timed
    public ResponseEntity<Void> deleteAgentProfile(@PathVariable Long id) {
        log.debug("REST request to delete AgentProfile : {}", id);
        agentProfileRepository.delete(id);
        agentProfileSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/agent-profiles?query=:query : search for the agentProfile corresponding
     * to the query.
     *
     * @param query the query of the agentProfile search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/agent-profiles")
    @Timed
    public ResponseEntity<List<AgentProfileDTO>> searchAgentProfiles(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of AgentProfiles for query {}", query);
        Page<AgentProfile> page = agentProfileSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/agent-profiles");
        return new ResponseEntity<>(agentProfileMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

}
