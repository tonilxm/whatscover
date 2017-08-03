package com.whatscover.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.whatscover.repository.InsuranceAgencyRepository;
import com.whatscover.web.rest.util.HeaderUtil;
import com.whatscover.web.rest.util.PaginationUtil;
import com.whatscover.service.InsuranceAgencyService;
import com.whatscover.service.dto.InsuranceAgencyDTO;
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

/**
 * REST controller for managing InsuranceAgency.
 */
@RestController
@RequestMapping("/api")
public class InsuranceAgencyResource {

    private final Logger log = LoggerFactory.getLogger(InsuranceAgencyResource.class);

    private static final String ENTITY_NAME = "insuranceAgency";

    private final InsuranceAgencyService insuranceAgencyService;
    
    private final InsuranceAgencyRepository insuranceAgencyRepository;

    public InsuranceAgencyResource(InsuranceAgencyService insuranceAgencyService, 
    		InsuranceAgencyRepository insuranceAgencyRepository) {
        this.insuranceAgencyService = insuranceAgencyService;
        this.insuranceAgencyRepository = insuranceAgencyRepository;
    }

    /**
     * POST  /insurance-agencies : Create a new insuranceAgency.
     *
     * @param insuranceAgencyDTO the insuranceAgencyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new insuranceAgencyDTO, or with status 400 (Bad Request) if the insuranceAgency has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/insurance-agencies")
    @Timed
    public ResponseEntity<InsuranceAgencyDTO> createInsuranceAgency(
    		@Valid @RequestBody InsuranceAgencyDTO insuranceAgencyDTO) 
    				throws URISyntaxException {
        log.debug("REST request to save InsuranceAgency : {}", insuranceAgencyDTO);
        if (insuranceAgencyDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new insuranceAgency cannot already have an ID")).body(null);
        } else if (insuranceAgencyRepository.findOneByCode(insuranceAgencyDTO.getCode()).isPresent()) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createMessageAlert(ENTITY_NAME,
					"messages.error.agencycodeexists", "Agency Code already in use"))
					.body(null);
		}
        InsuranceAgencyDTO result = insuranceAgencyService.save(insuranceAgencyDTO);
        return ResponseEntity.created(new URI("/api/insurance-agencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /insurance-agencies : Updates an existing insuranceAgency.
     *
     * @param insuranceAgencyDTO the insuranceAgencyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated insuranceAgencyDTO,
     * or with status 400 (Bad Request) if the insuranceAgencyDTO is not valid,
     * or with status 500 (Internal Server Error) if the insuranceAgencyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/insurance-agencies")
    @Timed
    public ResponseEntity<InsuranceAgencyDTO> updateInsuranceAgency(
    		@Valid @RequestBody InsuranceAgencyDTO insuranceAgencyDTO) 
    				throws URISyntaxException {
        log.debug("REST request to update InsuranceAgency : {}", insuranceAgencyDTO);
        if (insuranceAgencyDTO.getId() == null) {
            return createInsuranceAgency(insuranceAgencyDTO);
        }
        InsuranceAgencyDTO result = insuranceAgencyService.save(insuranceAgencyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, insuranceAgencyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /insurance-agencies : get all the insuranceAgencies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of insuranceAgencies in body
     */
    @GetMapping("/insurance-agencies")
    @Timed
    public ResponseEntity<List<InsuranceAgencyDTO>> getAllInsuranceAgencies(
    		@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of InsuranceAgencies");
        Page<InsuranceAgencyDTO> page = insuranceAgencyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/insurance-agencies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /insurance-agencies/:id : get the "id" insuranceAgency.
     *
     * @param id the id of the insuranceAgencyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the insuranceAgencyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/insurance-agencies/{id}")
    @Timed
    public ResponseEntity<InsuranceAgencyDTO> getInsuranceAgency(@PathVariable Long id) {
        log.debug("REST request to get InsuranceAgency : {}", id);
        InsuranceAgencyDTO insuranceAgencyDTO = insuranceAgencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(insuranceAgencyDTO));
    }

    /**
     * DELETE  /insurance-agencies/:id : delete the "id" insuranceAgency.
     *
     * @param id the id of the insuranceAgencyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/insurance-agencies/{id}")
    @Timed
    public ResponseEntity<Void> deleteInsuranceAgency(@PathVariable Long id) {
        log.debug("REST request to delete InsuranceAgency : {}", id);
        insuranceAgencyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/insurance-agencies?query=:query : search for the insuranceAgency corresponding
     * to the query.
     *
     * @param query the query of the insuranceAgency search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search-name/insurance-agencies")
    @Timed
    public ResponseEntity<List<InsuranceAgencyDTO>> searchInsuranceAgenciesByName(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of InsuranceAgencies by name {}", query);
        Page<InsuranceAgencyDTO> page = insuranceAgencyService.searchByName(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(
        		query, page, "/api/_search-name/insurance-agencies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
