package com.whatscover.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.whatscover.domain.User;
import com.whatscover.service.CustomerProfileService;
import com.whatscover.service.MailService;
import com.whatscover.service.UserService;
import com.whatscover.service.dto.CustomerProfileDTO;
import com.whatscover.service.exception.BusinessException;
import com.whatscover.service.util.RandomUtil;
import com.whatscover.web.rest.errors.BusinessErrorVM;
import com.whatscover.web.rest.util.HeaderUtil;
import com.whatscover.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * REST controller for managing CustomerProfile.
 */
@RestController
@RequestMapping("/api")
public class CustomerProfileResource {

    private final Logger log = LoggerFactory.getLogger(CustomerProfileResource.class);

    private static final String ENTITY_NAME = "customerProfile";

    private final CustomerProfileService customerProfileService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    public CustomerProfileResource(CustomerProfileService customerProfileService) {
        this.customerProfileService = customerProfileService;
    }

    /**
     * POST  /customer-profiles : Create a new customerProfile.
     *
     * @param customerProfileDTO the customerProfileDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerProfileDTO, or with status 400 (Bad Request) if the customerProfile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customer-profiles")
    @Timed
    public ResponseEntity createCustomerProfile(@Valid @RequestBody CustomerProfileDTO customerProfileDTO) throws URISyntaxException {
        log.debug("REST request to save CustomerProfile : {}", customerProfileDTO);
        if (customerProfileDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new customerProfile cannot already have an ID")).body(null);
        }

        CustomerProfileDTO result = null;

        try {
            // produce random password
            String randomPassword = RandomUtil.generatePassword();
            result = customerProfileService.create(customerProfileDTO, randomPassword);
            // Get recently created user
            User user = userService.findUserByEmail(customerProfileDTO.getEmail());
            // send activation email
            mailService.sendCustomerProfileActivationEmail(user,randomPassword);
        } catch (BusinessException be) {
            BusinessErrorVM beVM = new BusinessErrorVM(be.getMessage(), be.getMessage());
            return ResponseEntity.badRequest().body(beVM);
        }

        return ResponseEntity.created(new URI("/api/customer-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customer-profiles : Updates an existing customerProfile.
     *
     * @param customerProfileDTO the customerProfileDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customerProfileDTO,
     * or with status 400 (Bad Request) if the customerProfileDTO is not valid,
     * or with status 500 (Internal Server Error) if the customerProfileDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/customer-profiles")
    @Timed
    public ResponseEntity<CustomerProfileDTO> updateCustomerProfile(@Valid @RequestBody CustomerProfileDTO customerProfileDTO) throws URISyntaxException {
        log.debug("REST request to update CustomerProfile : {}", customerProfileDTO);
        if (customerProfileDTO.getId() == null) {
            return createCustomerProfile(customerProfileDTO);
        }
        CustomerProfileDTO result = customerProfileService.save(customerProfileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customerProfileDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /customer-profiles : get all the customerProfiles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of customerProfiles in body
     */
    @GetMapping("/customer-profiles")
    @Timed
    public ResponseEntity<List<CustomerProfileDTO>> getAllCustomerProfiles(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CustomerProfiles");
        Page<CustomerProfileDTO> page = customerProfileService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customer-profiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /customer-profiles/:id : get the "id" customerProfile.
     *
     * @param id the id of the customerProfileDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerProfileDTO, or with status 404 (Not Found)
     */
    @GetMapping("/customer-profiles/{id}")
    @Timed
    public ResponseEntity<CustomerProfileDTO> getCustomerProfile(@PathVariable Long id) {
        log.debug("REST request to get CustomerProfile : {}", id);
        CustomerProfileDTO customerProfileDTO = customerProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(customerProfileDTO));
    }

    /**
     * DELETE  /customer-profiles/:id : delete the "id" customerProfile.
     *
     * @param id the id of the customerProfileDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/customer-profiles/{id}")
    @Timed
    public ResponseEntity<Void> deleteCustomerProfile(@PathVariable Long id) {
        log.debug("REST request to delete CustomerProfile : {}", id);
        customerProfileService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/customer-profiles?query=:query : search for the customerProfile corresponding
     * to the query.
     *
     * @param query the query of the customerProfile search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search-name/customer-profiles")
    @Timed
    public ResponseEntity<List<CustomerProfileDTO>> searchCustomerProfilesByName(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of CustomerProfiles for query {}", query);
        Page<CustomerProfileDTO> page = customerProfileService.searchByName(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search-name/customer-profiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
