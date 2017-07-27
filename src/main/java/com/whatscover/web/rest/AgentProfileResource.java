package com.whatscover.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.whatscover.config.Constants;
import com.whatscover.domain.User;
import com.whatscover.repository.AgentProfileRepository;
import com.whatscover.security.AuthoritiesConstants;
import com.whatscover.service.AgentProfileService;
import com.whatscover.service.MailService;
import com.whatscover.service.UserService;
import com.whatscover.service.dto.AgentProfileDTO;
import com.whatscover.web.rest.util.HeaderUtil;
import com.whatscover.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing AgentProfile.
 */
@RestController
@RequestMapping("/api")
public class AgentProfileResource {

    private final Logger log = LoggerFactory.getLogger(AgentProfileResource.class);

    private static final String ENTITY_NAME = "agentProfile";
    
    private static final int PASSWORD_LENGTH = 6;

    private final AgentProfileService agentProfileService;
    
	private final AgentProfileRepository agentProfileRepository;
    
    private final MailService mailService;
    
    private final UserService userService;
 
    public AgentProfileResource(AgentProfileService agentProfileService, AgentProfileRepository agentProfileRepository, MailService mailService, UserService userService) {
        this.agentProfileService = agentProfileService;
        this.agentProfileRepository = agentProfileRepository;
        this.mailService = mailService;
        this.userService = userService;
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
        String email = agentProfileDTO.getEmail();
        int index = email.indexOf("@gmail.com");
		if (agentProfileDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists",
					"A new Agent Profile cannot already have an ID")).body(null);
		} else if (agentProfileRepository.findOneByAgentCode(agentProfileDTO.getAgent_code()).isPresent()) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createMessageAlert(ENTITY_NAME,
					"messages.error.agentcodeexists", "Agent Code already in use")).body(null);
		} else if (agentProfileRepository.findOneByEmail(email).isPresent()) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createMessageAlert(ENTITY_NAME,
					"messages.error.agentemailexists", "Agent Email already in use")).body(null);
		} else if (index < 1) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createMessageAlert(ENTITY_NAME,
					"messages.error.agentemailformat", "Agent Email wrong format")).body(null);
		}
        AgentProfileDTO result = agentProfileService.save(agentProfileDTO);
        User user = new User();
        String login = email.substring(0, index);
        String  password = getSaltString();
    	user = userService.createUser(login, password, agentProfileDTO.getFirst_name(), agentProfileDTO.getLast_name(), email, "" , "en");
        mailService.sendActivationEmail(user);
        return ResponseEntity.created(new URI("/api/agent-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < PASSWORD_LENGTH) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
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
        AgentProfileDTO result = agentProfileService.save(agentProfileDTO);
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
        Page<AgentProfileDTO> page = agentProfileService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/agent-profiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    @PostMapping("/send-email-profiles")
    @Timed
    public ResponseEntity<AgentProfileDTO> getSendEmailAgentProfiles(@Valid @RequestBody AgentProfileDTO agentProfileDTO) {
    	log.debug("REST request to Email AgentProfile : {}", agentProfileDTO);
    	if (agentProfileDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists",
					"A new Agent Profile cannot already have an ID")).body(null);
		} else if (agentProfileRepository.findOneByAgentCode(agentProfileDTO.getAgent_code()).isPresent()) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createMessageAlert(ENTITY_NAME,
					"messages.error.agentcodeexists", "Agent Code already in use"))
					.body(null);
		} else if(agentProfileRepository.findOneByEmail(agentProfileDTO.getEmail()).isPresent()) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createMessageAlert(ENTITY_NAME,
					"messages.error.agentemailexists", "Agent Email already in use"))
					.body(null);
		}
        mailService.sendEmail(agentProfileDTO.getEmail(), Constants.AGENT_EMAIL_SUBJECT_REGISTRATION, Constants.AGENT_EMAIL_SUBJECT_REGISTRATION, false, true);
        return ResponseEntity.ok().headers(HeaderUtil.createEntitySendEmailAlert(ENTITY_NAME, "Send Email Alert")).build();
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
        AgentProfileDTO agentProfileDTO = agentProfileService.findOne(id);
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
        agentProfileService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/agent-profiles?query=:query : search for the agentProfile corresponding
     * to the query.
     *
     * @param queryData the query of the agentProfile search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/agent-profiles")
    @Timed
    public ResponseEntity<List<AgentProfileDTO>> searchAgentProfiles(
    		@RequestParam(value="queryData") String[] queryData,
    		@ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of AgentProfiles for query {}", queryData.toString());
        Page<AgentProfileDTO> page = agentProfileService.search(queryData, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(
        		queryData.toString(), page, "/api/_search/agent-profiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
