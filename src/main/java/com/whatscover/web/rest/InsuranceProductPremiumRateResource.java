package com.whatscover.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.whatscover.service.InsuranceProductPremiumRateService;
import com.whatscover.web.rest.util.HeaderUtil;
import com.whatscover.web.rest.util.PaginationUtil;
import com.whatscover.service.dto.InsuranceProductPremiumRateDTO;
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
 * REST controller for managing InsuranceProductPremiumRate.
 */
@RestController
@RequestMapping("/api")
public class InsuranceProductPremiumRateResource {

    private final Logger log = LoggerFactory.getLogger(InsuranceProductPremiumRateResource.class);

    private static final String ENTITY_NAME = "insuranceProductPremiumRate";

    private final InsuranceProductPremiumRateService insuranceProductPremiumRateService;

    public InsuranceProductPremiumRateResource(InsuranceProductPremiumRateService insuranceProductPremiumRateService) {
        this.insuranceProductPremiumRateService = insuranceProductPremiumRateService;
    }

    /**
     * POST  /insurance-product-premium-rates : Create a new insuranceProductPremiumRate.
     *
     * @param insuranceProductPremiumRateDTO the insuranceProductPremiumRateDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new insuranceProductPremiumRateDTO, or with status 400 (Bad Request) if the insuranceProductPremiumRate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/insurance-product-premium-rates")
    @Timed
    public ResponseEntity<InsuranceProductPremiumRateDTO> createInsuranceProductPremiumRate(@Valid @RequestBody InsuranceProductPremiumRateDTO insuranceProductPremiumRateDTO) throws URISyntaxException {
        log.debug("REST request to save InsuranceProductPremiumRate : {}", insuranceProductPremiumRateDTO);
        if (insuranceProductPremiumRateDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new insuranceProductPremiumRate cannot already have an ID")).body(null);
        }
        InsuranceProductPremiumRateDTO result = insuranceProductPremiumRateService.save(insuranceProductPremiumRateDTO);
        return ResponseEntity.created(new URI("/api/insurance-product-premium-rates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /insurance-product-premium-rates : Updates an existing insuranceProductPremiumRate.
     *
     * @param insuranceProductPremiumRateDTO the insuranceProductPremiumRateDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated insuranceProductPremiumRateDTO,
     * or with status 400 (Bad Request) if the insuranceProductPremiumRateDTO is not valid,
     * or with status 500 (Internal Server Error) if the insuranceProductPremiumRateDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/insurance-product-premium-rates")
    @Timed
    public ResponseEntity<InsuranceProductPremiumRateDTO> updateInsuranceProductPremiumRate(@Valid @RequestBody InsuranceProductPremiumRateDTO insuranceProductPremiumRateDTO) throws URISyntaxException {
        log.debug("REST request to update InsuranceProductPremiumRate : {}", insuranceProductPremiumRateDTO);
        if (insuranceProductPremiumRateDTO.getId() == null) {
            return createInsuranceProductPremiumRate(insuranceProductPremiumRateDTO);
        }
        InsuranceProductPremiumRateDTO result = insuranceProductPremiumRateService.save(insuranceProductPremiumRateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, insuranceProductPremiumRateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /insurance-product-premium-rates : get all the insuranceProductPremiumRates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of insuranceProductPremiumRates in body
     */
    @GetMapping("/insurance-product-premium-rates")
    @Timed
    public ResponseEntity<List<InsuranceProductPremiumRateDTO>> getAllInsuranceProductPremiumRates(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of InsuranceProductPremiumRates");
        Page<InsuranceProductPremiumRateDTO> page = insuranceProductPremiumRateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/insurance-product-premium-rates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /insurance-product-premium-rates/:id : get the "id" insuranceProductPremiumRate.
     *
     * @param id the id of the insuranceProductPremiumRateDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the insuranceProductPremiumRateDTO, or with status 404 (Not Found)
     */
    @GetMapping("/insurance-product-premium-rates/{id}")
    @Timed
    public ResponseEntity<InsuranceProductPremiumRateDTO> getInsuranceProductPremiumRate(@PathVariable Long id) {
        log.debug("REST request to get InsuranceProductPremiumRate : {}", id);
        InsuranceProductPremiumRateDTO insuranceProductPremiumRateDTO = insuranceProductPremiumRateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(insuranceProductPremiumRateDTO));
    }

    /**
     * DELETE  /insurance-product-premium-rates/:id : delete the "id" insuranceProductPremiumRate.
     *
     * @param id the id of the insuranceProductPremiumRateDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/insurance-product-premium-rates/{id}")
    @Timed
    public ResponseEntity<Void> deleteInsuranceProductPremiumRate(@PathVariable Long id) {
        log.debug("REST request to delete InsuranceProductPremiumRate : {}", id);
        insuranceProductPremiumRateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/insurance-product-premium-rates?query=:query : search for the insuranceProductPremiumRate corresponding
     * to the query.
     *
     * @param query the query of the insuranceProductPremiumRate search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/insurance-product-premium-rates")
    @Timed
    public ResponseEntity<List<InsuranceProductPremiumRateDTO>> searchInsuranceProductPremiumRates(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of InsuranceProductPremiumRates for query {}", query);
        Page<InsuranceProductPremiumRateDTO> page = insuranceProductPremiumRateService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/insurance-product-premium-rates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * SEARCH  /_search-by-productid/insurance-product-premium-rates?query=:query : search for the insuranceProductPremiumRate corresponding
     * to the query.
     *
     * @param query the query of the insuranceProductPremiumRate search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search-by-productid/insurance-product-premium-rates")
    @Timed
    public ResponseEntity<List<InsuranceProductPremiumRateDTO>> searchInsuranceProductPremiumRatesByProductId(@RequestParam Long query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of InsuranceProductPremiumRatesByProductId for query {}", query);
        Page<InsuranceProductPremiumRateDTO> page = insuranceProductPremiumRateService.searchByInsuranceProductId(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(String.valueOf(query), page, "/_search-by-productid/insurance-product-premium-rates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
