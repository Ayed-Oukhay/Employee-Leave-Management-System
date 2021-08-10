package com.projet.gestionconge.web.rest;

import com.projet.gestionconge.domain.Salarie;
import com.projet.gestionconge.repository.SalarieRepository;
import com.projet.gestionconge.service.SalarieService;
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

/**
 * REST controller for managing {@link com.projet.gestionconge.domain.Salarie}.
 */
@RestController
@RequestMapping("/api")
public class SalarieResource {

    private final Logger log = LoggerFactory.getLogger(SalarieResource.class);

    private static final String ENTITY_NAME = "salarie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SalarieService salarieService;

    private final SalarieRepository salarieRepository;

    public SalarieResource(SalarieService salarieService, SalarieRepository salarieRepository) {
        this.salarieService = salarieService;
        this.salarieRepository = salarieRepository;
    }

    /**
     * {@code POST  /salaries} : Create a new salarie.
     *
     * @param salarie the salarie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salarie, or with status {@code 400 (Bad Request)} if the salarie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/salaries")
    public ResponseEntity<Salarie> createSalarie(@RequestBody Salarie salarie) throws URISyntaxException {
        log.debug("REST request to save Salarie : {}", salarie);
        if (salarie.getId() != null) {
            throw new BadRequestAlertException("A new salarie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Salarie result = salarieService.save(salarie);
        return ResponseEntity
            .created(new URI("/api/salaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /salaries/:id} : Updates an existing salarie.
     *
     * @param id the id of the salarie to save.
     * @param salarie the salarie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salarie,
     * or with status {@code 400 (Bad Request)} if the salarie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salarie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/salaries/{id}")
    public ResponseEntity<Salarie> updateSalarie(@PathVariable(value = "id", required = false) final Long id, @RequestBody Salarie salarie)
        throws URISyntaxException {
        log.debug("REST request to update Salarie : {}, {}", id, salarie);
        if (salarie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salarie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salarieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Salarie result = salarieService.save(salarie);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salarie.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /salaries/:id} : Partial updates given fields of an existing salarie, field will ignore if it is null
     *
     * @param id the id of the salarie to save.
     * @param salarie the salarie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salarie,
     * or with status {@code 400 (Bad Request)} if the salarie is not valid,
     * or with status {@code 404 (Not Found)} if the salarie is not found,
     * or with status {@code 500 (Internal Server Error)} if the salarie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/salaries/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Salarie> partialUpdateSalarie(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Salarie salarie
    ) throws URISyntaxException {
        log.debug("REST request to partial update Salarie partially : {}, {}", id, salarie);
        if (salarie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salarie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salarieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Salarie> result = salarieService.partialUpdate(salarie);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salarie.getId().toString())
        );
    }

    /**
     * {@code GET  /salaries} : get all the salaries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salaries in body.
     */
    @GetMapping("/salaries")
    public ResponseEntity<List<Salarie>> getAllSalaries(Pageable pageable) {
        log.debug("REST request to get a page of Salaries");
        Page<Salarie> page = salarieService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /salaries/:id} : get the "id" salarie.
     *
     * @param id the id of the salarie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salarie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/salaries/{id}")
    public ResponseEntity<Salarie> getSalarie(@PathVariable Long id) {
        log.debug("REST request to get Salarie : {}", id);
        Optional<Salarie> salarie = salarieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(salarie);
    }

    /**
     * {@code DELETE  /salaries/:id} : delete the "id" salarie.
     *
     * @param id the id of the salarie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/salaries/{id}")
    public ResponseEntity<Void> deleteSalarie(@PathVariable Long id) {
        log.debug("REST request to delete Salarie : {}", id);
        salarieService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
