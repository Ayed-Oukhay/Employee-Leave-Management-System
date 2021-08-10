package com.projet.gestionconge.web.rest;

import com.projet.gestionconge.domain.TypeContrat;
import com.projet.gestionconge.repository.TypeContratRepository;
import com.projet.gestionconge.service.TypeContratService;
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
 * REST controller for managing {@link com.projet.gestionconge.domain.TypeContrat}.
 */
@RestController
@RequestMapping("/api")
public class TypeContratResource {

    private final Logger log = LoggerFactory.getLogger(TypeContratResource.class);

    private static final String ENTITY_NAME = "typeContrat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeContratService typeContratService;

    private final TypeContratRepository typeContratRepository;

    public TypeContratResource(TypeContratService typeContratService, TypeContratRepository typeContratRepository) {
        this.typeContratService = typeContratService;
        this.typeContratRepository = typeContratRepository;
    }

    /**
     * {@code POST  /type-contrats} : Create a new typeContrat.
     *
     * @param typeContrat the typeContrat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeContrat, or with status {@code 400 (Bad Request)} if the typeContrat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-contrats")
    public ResponseEntity<TypeContrat> createTypeContrat(@RequestBody TypeContrat typeContrat) throws URISyntaxException {
        log.debug("REST request to save TypeContrat : {}", typeContrat);
        if (typeContrat.getId() != null) {
            throw new BadRequestAlertException("A new typeContrat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeContrat result = typeContratService.save(typeContrat);
        return ResponseEntity
            .created(new URI("/api/type-contrats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-contrats/:id} : Updates an existing typeContrat.
     *
     * @param id the id of the typeContrat to save.
     * @param typeContrat the typeContrat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeContrat,
     * or with status {@code 400 (Bad Request)} if the typeContrat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeContrat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-contrats/{id}")
    public ResponseEntity<TypeContrat> updateTypeContrat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeContrat typeContrat
    ) throws URISyntaxException {
        log.debug("REST request to update TypeContrat : {}, {}", id, typeContrat);
        if (typeContrat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeContrat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeContratRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeContrat result = typeContratService.save(typeContrat);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeContrat.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-contrats/:id} : Partial updates given fields of an existing typeContrat, field will ignore if it is null
     *
     * @param id the id of the typeContrat to save.
     * @param typeContrat the typeContrat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeContrat,
     * or with status {@code 400 (Bad Request)} if the typeContrat is not valid,
     * or with status {@code 404 (Not Found)} if the typeContrat is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeContrat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-contrats/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TypeContrat> partialUpdateTypeContrat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeContrat typeContrat
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeContrat partially : {}, {}", id, typeContrat);
        if (typeContrat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeContrat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeContratRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeContrat> result = typeContratService.partialUpdate(typeContrat);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeContrat.getId().toString())
        );
    }

    /**
     * {@code GET  /type-contrats} : get all the typeContrats.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeContrats in body.
     */
    @GetMapping("/type-contrats")
    public ResponseEntity<List<TypeContrat>> getAllTypeContrats(Pageable pageable) {
        log.debug("REST request to get a page of TypeContrats");
        Page<TypeContrat> page = typeContratService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-contrats/:id} : get the "id" typeContrat.
     *
     * @param id the id of the typeContrat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeContrat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-contrats/{id}")
    public ResponseEntity<TypeContrat> getTypeContrat(@PathVariable Long id) {
        log.debug("REST request to get TypeContrat : {}", id);
        Optional<TypeContrat> typeContrat = typeContratService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeContrat);
    }

    /**
     * {@code DELETE  /type-contrats/:id} : delete the "id" typeContrat.
     *
     * @param id the id of the typeContrat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-contrats/{id}")
    public ResponseEntity<Void> deleteTypeContrat(@PathVariable Long id) {
        log.debug("REST request to delete TypeContrat : {}", id);
        typeContratService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
