package com.whatscover.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.whatscover.service.InsuranceCompanyService;
import com.whatscover.web.rest.util.HeaderUtil;
import com.whatscover.web.rest.util.PaginationUtil;
import com.whatscover.service.dto.InsuranceCompanyDTO;
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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing InsuranceCompany.
 */
@RestController
@RequestMapping("/api")
public class InsuranceCompanyResource {

    private final Logger log = LoggerFactory.getLogger(InsuranceCompanyResource.class);

    private static final String ENTITY_NAME = "insuranceCompany";

    private final InsuranceCompanyService insuranceCompanyService;

    public InsuranceCompanyResource(InsuranceCompanyService insuranceCompanyService) {
        this.insuranceCompanyService = insuranceCompanyService;
    }

    /**
     * POST  /insurance-companies : Create a new insuranceCompany.
     *
     * @param insuranceCompanyDTO the insuranceCompanyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new insuranceCompanyDTO, or with status 400 (Bad Request) if the insuranceCompany has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/insurance-companies")
    @Timed
    public ResponseEntity<InsuranceCompanyDTO> createInsuranceCompany(@Valid @RequestBody InsuranceCompanyDTO insuranceCompanyDTO) throws URISyntaxException {
        log.debug("REST request to save InsuranceCompany : {}", insuranceCompanyDTO);
        if (insuranceCompanyDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new insuranceCompany cannot already have an ID")).body(null);
        }
        InsuranceCompanyDTO result = insuranceCompanyService.save(insuranceCompanyDTO);
        return ResponseEntity.created(new URI("/api/insurance-companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /insurance-companies : Updates an existing insuranceCompany.
     *
     * @param insuranceCompanyDTO the insuranceCompanyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated insuranceCompanyDTO,
     * or with status 400 (Bad Request) if the insuranceCompanyDTO is not valid,
     * or with status 500 (Internal Server Error) if the insuranceCompanyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/insurance-companies")
    @Timed
    public ResponseEntity<InsuranceCompanyDTO> updateInsuranceCompany(@Valid @RequestBody InsuranceCompanyDTO insuranceCompanyDTO) throws URISyntaxException {
        log.debug("REST request to update InsuranceCompany : {}", insuranceCompanyDTO);
        if (insuranceCompanyDTO.getId() == null) {
            return createInsuranceCompany(insuranceCompanyDTO);
        }
        InsuranceCompanyDTO result = insuranceCompanyService.save(insuranceCompanyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, insuranceCompanyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /insurance-companies : get all the insuranceCompanies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of insuranceCompanies in body
     */
    @GetMapping("/insurance-companies")
    @Timed
    public ResponseEntity<List<InsuranceCompanyDTO>> getAllInsuranceCompanies(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of InsuranceCompanies");
        Page<InsuranceCompanyDTO> page = insuranceCompanyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/insurance-companies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /insurance-companies/:id : get the "id" insuranceCompany.
     *
     * @param id the id of the insuranceCompanyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the insuranceCompanyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/insurance-companies/{id}")
    @Timed
    public ResponseEntity<InsuranceCompanyDTO> getInsuranceCompany(@PathVariable Long id) {
        log.debug("REST request to get InsuranceCompany : {}", id);
        InsuranceCompanyDTO insuranceCompanyDTO = insuranceCompanyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(insuranceCompanyDTO));
    }

    /**
     * DELETE  /insurance-companies/:id : delete the "id" insuranceCompany.
     *
     * @param id the id of the insuranceCompanyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/insurance-companies/{id}")
    @Timed
    public ResponseEntity<Void> deleteInsuranceCompany(@PathVariable Long id) {
        log.debug("REST request to delete InsuranceCompany : {}", id);
        insuranceCompanyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/insurance-companies?query=:query : search for the insuranceCompany corresponding
     * to the query.
     *
     * @param query the query of the insuranceCompany search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/insurance-companies")
    @Timed
    public ResponseEntity<List<InsuranceCompanyDTO>> searchInsuranceCompanies(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of InsuranceCompanies for query {}", query);
        Page<InsuranceCompanyDTO> page = insuranceCompanyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/insurance-companies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
