package com.whatscover.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.whatscover.domain.InsuaranceAgency;

import com.whatscover.repository.InsuaranceAgencyRepository;
import com.whatscover.repository.search.InsuaranceAgencySearchRepository;
import com.whatscover.web.rest.util.HeaderUtil;
import com.whatscover.web.rest.util.PaginationUtil;
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
 * REST controller for managing InsuaranceAgency.
 */
@RestController
@RequestMapping("/api")
public class InsuaranceAgencyResource {

    private final Logger log = LoggerFactory.getLogger(InsuaranceAgencyResource.class);

    private static final String ENTITY_NAME = "insuaranceAgency";

    private final InsuaranceAgencyRepository insuaranceAgencyRepository;

    private final InsuaranceAgencySearchRepository insuaranceAgencySearchRepository;

    public InsuaranceAgencyResource(InsuaranceAgencyRepository insuaranceAgencyRepository, InsuaranceAgencySearchRepository insuaranceAgencySearchRepository) {
        this.insuaranceAgencyRepository = insuaranceAgencyRepository;
        this.insuaranceAgencySearchRepository = insuaranceAgencySearchRepository;
    }

    /**
     * POST  /insuarance-agencies : Create a new insuaranceAgency.
     *
     * @param insuaranceAgency the insuaranceAgency to create
     * @return the ResponseEntity with status 201 (Created) and with body the new insuaranceAgency, or with status 400 (Bad Request) if the insuaranceAgency has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/insuarance-agencies")
    @Timed
    public ResponseEntity<InsuaranceAgency> createInsuaranceAgency(@Valid @RequestBody InsuaranceAgency insuaranceAgency) throws URISyntaxException {
        log.debug("REST request to save InsuaranceAgency : {}", insuaranceAgency);
        if (insuaranceAgency.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new insuaranceAgency cannot already have an ID")).body(null);
        }
        InsuaranceAgency result = insuaranceAgencyRepository.save(insuaranceAgency);
        insuaranceAgencySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/insuarance-agencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /insuarance-agencies : Updates an existing insuaranceAgency.
     *
     * @param insuaranceAgency the insuaranceAgency to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated insuaranceAgency,
     * or with status 400 (Bad Request) if the insuaranceAgency is not valid,
     * or with status 500 (Internal Server Error) if the insuaranceAgency couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/insuarance-agencies")
    @Timed
    public ResponseEntity<InsuaranceAgency> updateInsuaranceAgency(@Valid @RequestBody InsuaranceAgency insuaranceAgency) throws URISyntaxException {
        log.debug("REST request to update InsuaranceAgency : {}", insuaranceAgency);
        if (insuaranceAgency.getId() == null) {
            return createInsuaranceAgency(insuaranceAgency);
        }
        InsuaranceAgency result = insuaranceAgencyRepository.save(insuaranceAgency);
        insuaranceAgencySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, insuaranceAgency.getId().toString()))
            .body(result);
    }

    /**
     * GET  /insuarance-agencies : get all the insuaranceAgencies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of insuaranceAgencies in body
     */
    @GetMapping("/insuarance-agencies")
    @Timed
    public ResponseEntity<List<InsuaranceAgency>> getAllInsuaranceAgencies(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of InsuaranceAgencies");
        Page<InsuaranceAgency> page = insuaranceAgencyRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/insuarance-agencies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /insuarance-agencies/:id : get the "id" insuaranceAgency.
     *
     * @param id the id of the insuaranceAgency to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the insuaranceAgency, or with status 404 (Not Found)
     */
    @GetMapping("/insuarance-agencies/{id}")
    @Timed
    public ResponseEntity<InsuaranceAgency> getInsuaranceAgency(@PathVariable Long id) {
        log.debug("REST request to get InsuaranceAgency : {}", id);
        InsuaranceAgency insuaranceAgency = insuaranceAgencyRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(insuaranceAgency));
    }

    /**
     * DELETE  /insuarance-agencies/:id : delete the "id" insuaranceAgency.
     *
     * @param id the id of the insuaranceAgency to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/insuarance-agencies/{id}")
    @Timed
    public ResponseEntity<Void> deleteInsuaranceAgency(@PathVariable Long id) {
        log.debug("REST request to delete InsuaranceAgency : {}", id);
        insuaranceAgencyRepository.delete(id);
        insuaranceAgencySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/insuarance-agencies?query=:query : search for the insuaranceAgency corresponding
     * to the query.
     *
     * @param query the query of the insuaranceAgency search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/insuarance-agencies")
    @Timed
    public ResponseEntity<List<InsuaranceAgency>> searchInsuaranceAgencies(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of InsuaranceAgencies for query {}", query);
        Page<InsuaranceAgency> page = insuaranceAgencySearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/insuarance-agencies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
