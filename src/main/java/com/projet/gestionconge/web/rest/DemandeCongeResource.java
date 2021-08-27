package com.projet.gestionconge.web.rest;

import com.projet.gestionconge.domain.DemandeConge;
import com.projet.gestionconge.domain.Salarie;
import com.projet.gestionconge.repository.SalarieRepository;
import com.projet.gestionconge.repository.DemandeCongeRepository;
import com.projet.gestionconge.service.DemandeCongeService;
import com.projet.gestionconge.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
//import org.springframework.security.*;
import java.util.*;

/**
 * REST controller for managing {@link com.projet.gestionconge.domain.DemandeConge}.
 */
@RestController
@RequestMapping("/api")
public class DemandeCongeResource {

    private final Logger log = LoggerFactory.getLogger(DemandeCongeResource.class);

    private static final String ENTITY_NAME = "demandeConge";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DemandeCongeService demandeCongeService;

    private final DemandeCongeRepository demandeCongeRepository;

    private final SalarieRepository salarieRepository;

    public DemandeCongeResource(DemandeCongeService demandeCongeService, DemandeCongeRepository demandeCongeRepository, SalarieRepository salarieRepository) {
        this.demandeCongeService = demandeCongeService;
        this.demandeCongeRepository = demandeCongeRepository;
        this.salarieRepository = salarieRepository;
    }

    /**
     * {@code POST  /demande-conges} : Create a new demandeConge.
     *
     * @param demandeConge the demandeConge to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new demandeConge, or with status {@code 400 (Bad Request)} if the demandeConge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/demande-conges")
    public ResponseEntity<DemandeConge> createDemandeConge(@RequestBody DemandeConge demandeConge) throws URISyntaxException {
        log.debug("REST request to save DemandeConge : {}", demandeConge);
        if (demandeConge.getId() != null) {
            throw new BadRequestAlertException("A new demandeConge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DemandeConge result = demandeCongeService.save(demandeConge);
        return ResponseEntity
            .created(new URI("/api/demande-conges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /demande-conges/:id} : Updates an existing demandeConge.
     *
     * @param id the id of the demandeConge to save.
     * @param demandeConge the demandeConge to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeConge,
     * or with status {@code 400 (Bad Request)} if the demandeConge is not valid,
     * or with status {@code 500 (Internal Server Error)} if the demandeConge couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/demande-conges/{id}")
    public ResponseEntity<DemandeConge> updateDemandeConge(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DemandeConge demandeConge
    ) throws URISyntaxException {
        log.debug("REST request to update DemandeConge : {}, {}", id, demandeConge);
        if (demandeConge.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeConge.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeCongeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DemandeConge result = demandeCongeService.save(demandeConge);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, demandeConge.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /demande-conges/:id} : Partial updates given fields of an existing demandeConge, field will ignore if it is null
     *
     * @param id the id of the demandeConge to save.
     * @param demandeConge the demandeConge to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeConge,
     * or with status {@code 400 (Bad Request)} if the demandeConge is not valid,
     * or with status {@code 404 (Not Found)} if the demandeConge is not found,
     * or with status {@code 500 (Internal Server Error)} if the demandeConge couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/demande-conges/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DemandeConge> partialUpdateDemandeConge(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DemandeConge demandeConge
    ) throws URISyntaxException {
        log.debug("REST request to partial update DemandeConge partially : {}, {}", id, demandeConge);
        if (demandeConge.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeConge.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeCongeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DemandeConge> result = demandeCongeService.partialUpdate(demandeConge);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, demandeConge.getId().toString())
        );
    }

    /**
     * {@code GET  /demande-conges} : get all the demandeConges.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demandeConges in body.
     */
    @GetMapping("/demande-conges")
    public ResponseEntity<List<DemandeConge>> getAllDemandeConges(Pageable pageable) {
        log.debug("REST request to get a page of DemandeConges");
        Page<DemandeConge> page = demandeCongeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /demande-specific-conges} : get demandeConges based on the connected admin.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demandeConges in body.
     */
    //@GetMapping("/demande-specific-conges")
    //public ResponseEntity<List<DemandeConge>> getSpecificDemandeConges(Pageable pageable) {
        //log.debug("REST request to get a page of specific DemandeConges");
        /* Getting the current connected user */
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        // String CurrentUser = auth.getFirstName();
        // log.debug(CurrentUser);
        /* Filtering the list of demandes based on the current connected user */
        //Page<DemandeConge> page = new Page<DemandeConge>(); //empty list which will contain the filtered final list
        //Page<DemandeConge> allDemandes = demandeCongeService.findAll(pageable); //Get all demandes in order to go through them
        /* for (DemandeConge d:allDemandes){
            Long idS = d.getSalarie().getId(); //get the id of the salarie that passed the demand
            Optional<Salarie> s = salarieRepository.findById(idS); //get that salarie from the list of the salaries
            String manager = s.get().getManager(); //get the manager of that salarie
            if (manager=="admin"){
                page.add(d); //add the demand into the validation page
            }
        } */
        /* HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent()); //return the page */
    //}

    /* final int start = (int)pageable.getOffset();
    final int end = Math.min((start + pageable.getPageSize()), users.size());
    final Page<User> page = new PageImpl<>(users.subList(start, end), pageable, users.size()); */

    /**
     * {@code GET  /demande-conges/:id} : get the "id" demandeConge.
     *
     * @param id the id of the demandeConge to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the demandeConge, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/demande-conges/{id}")
    public ResponseEntity<DemandeConge> getDemandeConge(@PathVariable Long id) {
        log.debug("REST request to get DemandeConge : {}", id);
        Optional<DemandeConge> demandeConge = demandeCongeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(demandeConge);
    }

    /**
     * {@code DELETE  /demande-conges/:id} : delete the "id" demandeConge.
     *
     * @param id the id of the demandeConge to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/demande-conges/{id}")
    public ResponseEntity<Void> deleteDemandeConge(@PathVariable Long id) {
        log.debug("REST request to delete DemandeConge : {}", id);
        demandeCongeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
