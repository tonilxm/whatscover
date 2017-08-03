package com.whatscover.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.whatscover.service.InsuranceProductPremiumRateService;
import com.whatscover.service.InsuranceProductService;
import com.whatscover.web.rest.util.HeaderUtil;
import com.whatscover.web.rest.util.JsonConverter;
import com.whatscover.web.rest.util.PaginationUtil;
import com.whatscover.service.dto.InsuranceProductDTO;
import com.whatscover.service.dto.InsuranceProductPremiumRateDTO;

import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing InsuranceProduct.
 */
@RestController
@RequestMapping("/api")
public class InsuranceProductResource {

    private final Logger log = LoggerFactory.getLogger(InsuranceProductResource.class);

    private static final String ENTITY_NAME = "insuranceProduct";

    private final InsuranceProductService insuranceProductService;
    private final InsuranceProductPremiumRateService insuranceProductPremiumRateService;
    private final JsonConverter converter;

    public InsuranceProductResource(InsuranceProductService insuranceProductService, 
    		InsuranceProductPremiumRateService insuranceProductPremiumRateService, JsonConverter converter) {
        this.insuranceProductService = insuranceProductService;
        this.insuranceProductPremiumRateService = insuranceProductPremiumRateService;
        this.converter = converter;
    }

    /**
     * POST  /insurance-products : Create a new insuranceProduct.
     *
     * @param insuranceProductDTO the insuranceProductDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new insuranceProductDTO, or with status 400 (Bad Request) if the insuranceProduct has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/insurance-products")
    @Timed
    public ResponseEntity<InsuranceProductDTO> createInsuranceProduct(@Valid @RequestBody InsuranceProductDTO insuranceProductDTO) throws URISyntaxException {
        log.debug("REST request to save InsuranceProduct : {}", insuranceProductDTO);
        if (insuranceProductDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new insuranceProduct cannot already have an ID")).body(null);
        }
        InsuranceProductDTO result = insuranceProductService.save(insuranceProductDTO);
        return ResponseEntity.created(new URI("/api/insurance-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    @PostMapping("/saveInsuranceProduct")
    @ResponseBody
    public ResponseEntity<InsuranceProductDTO> saveInsuranceProduct(@RequestBody ObjectNode data) throws URISyntaxException {
    	log.debug("REST request to save InsuranceProduct : {}", data);
    	InsuranceProductDTO insuranceProductDTO = (InsuranceProductDTO) converter.fromJson(converter.toJson(data), InsuranceProductDTO.class);
    	log.debug("insurance product : {}", insuranceProductDTO);
    	if (insuranceProductDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new insuranceProduct cannot already have an ID")).body(null);
        }
    	InsuranceProductDTO result = insuranceProductService.save(insuranceProductDTO);
    	
    	List<InsuranceProductPremiumRateDTO> premiumRateDTOs = (List<InsuranceProductPremiumRateDTO>) converter.fromJson(converter.toJson(data.get("premiumRates")), 
    			converter.constructParametricType(List.class, InsuranceProductPremiumRateDTO.class));
    	log.debug("Premium Rate : {}", premiumRateDTOs);
    	insuranceProductPremiumRateService.saveEntities(premiumRateDTOs, result.getId());
        
        return ResponseEntity.created(new URI("/api/insurance-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
        
        //InsuranceProductDTO result = insuranceProductService.save(insuranceProductDTO);
        /*return ResponseEntity.created(new URI("/api/insurance-products/" + 100))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, "100"))
            .body(new InsuranceProductDTO());*/
    }
    
    @PutMapping("/saveInsuranceProduct")
    @Timed
    public ResponseEntity<InsuranceProductDTO> updateInsuranceProduct(@RequestBody ObjectNode data) throws URISyntaxException {
    	log.debug("REST request to save InsuranceProduct : {}", data);
    	InsuranceProductDTO insuranceProductDTO = (InsuranceProductDTO) converter.fromJson(converter.toJson(data), InsuranceProductDTO.class);
    	log.debug("insurance product : {}", insuranceProductDTO);
        if (insuranceProductDTO.getId() == null) {
            return createInsuranceProduct(insuranceProductDTO);
        }
        InsuranceProductDTO result = insuranceProductService.save(insuranceProductDTO);
        
        List<InsuranceProductPremiumRateDTO> premiumRateDTOs = (List<InsuranceProductPremiumRateDTO>) converter.fromJson(converter.toJson(data.get("premiumRates")), 
    			converter.constructParametricType(List.class, InsuranceProductPremiumRateDTO.class));
    	log.debug("Premium Rate : {}", premiumRateDTOs);
    	insuranceProductPremiumRateService.saveEntities(premiumRateDTOs, result.getId());
    	
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, insuranceProductDTO.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /insurance-products : Updates an existing insuranceProduct.
     *
     * @param insuranceProductDTO the insuranceProductDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated insuranceProductDTO,
     * or with status 400 (Bad Request) if the insuranceProductDTO is not valid,
     * or with status 500 (Internal Server Error) if the insuranceProductDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/insurance-products")
    @Timed
    public ResponseEntity<InsuranceProductDTO> updateInsuranceProduct(@Valid @RequestBody InsuranceProductDTO insuranceProductDTO) throws URISyntaxException {
        log.debug("REST request to update InsuranceProduct : {}", insuranceProductDTO);
        if (insuranceProductDTO.getId() == null) {
            return createInsuranceProduct(insuranceProductDTO);
        }
        InsuranceProductDTO result = insuranceProductService.save(insuranceProductDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, insuranceProductDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /insurance-products : get all the insuranceProducts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of insuranceProducts in body
     */
    @GetMapping("/insurance-products")
    @Timed
    public ResponseEntity<List<InsuranceProductDTO>> getAllInsuranceProducts(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of InsuranceProducts");
        Page<InsuranceProductDTO> page = insuranceProductService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/insurance-products");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /insurance-products/:id : get the "id" insuranceProduct.
     *
     * @param id the id of the insuranceProductDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the insuranceProductDTO, or with status 404 (Not Found)
     */
    @GetMapping("/insurance-products/{id}")
    @Timed
    public ResponseEntity<InsuranceProductDTO> getInsuranceProduct(@PathVariable Long id) {
        log.debug("REST request to get InsuranceProduct : {}", id);
        InsuranceProductDTO insuranceProductDTO = insuranceProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(insuranceProductDTO));
    }

    /**
     * DELETE  /insurance-products/:id : delete the "id" insuranceProduct.
     *
     * @param id the id of the insuranceProductDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/insurance-products/{id}")
    @Timed
    public ResponseEntity<Void> deleteInsuranceProduct(@PathVariable Long id) {
        log.debug("REST request to delete InsuranceProduct : {}", id);
        insuranceProductService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/insurance-products?query=:query : search for the insuranceProduct corresponding
     * to the query.
     *
     * @param query the query of the insuranceProduct search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/insurance-products")
    @Timed
    public ResponseEntity<List<InsuranceProductDTO>> searchInsuranceProducts(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of InsuranceProducts for query {}", query);
        Page<InsuranceProductDTO> page = insuranceProductService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/insurance-products");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
