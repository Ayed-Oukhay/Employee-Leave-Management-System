package com.projet.gestionconge.web.rest;

import com.projet.gestionconge.domain.TypeConge;
import com.projet.gestionconge.repository.TypeCongeRepository;
import com.projet.gestionconge.service.TypeCongeService;
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
 * REST controller for managing {@link com.projet.gestionconge.domain.TypeConge}.
 */
@RestController
@RequestMapping("/api")
public class TypeCongeResource {

    private final Logger log = LoggerFactory.getLogger(TypeCongeResource.class);

    private static final String ENTITY_NAME = "typeConge";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeCongeService typeCongeService;

    private final TypeCongeRepository typeCongeRepository;

    public TypeCongeResource(TypeCongeService typeCongeService, TypeCongeRepository typeCongeRepository) {
        this.typeCongeService = typeCongeService;
        this.typeCongeRepository = typeCongeRepository;
    }

    /**
     * {@code POST  /type-conges} : Create a new typeConge.
     *
     * @param typeConge the typeConge to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeConge, or with status {@code 400 (Bad Request)} if the typeConge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-conges")
    public ResponseEntity<TypeConge> createTypeConge(@RequestBody TypeConge typeConge) throws URISyntaxException {
        log.debug("REST request to save TypeConge : {}", typeConge);
        if (typeConge.getId() != null) {
            throw new BadRequestAlertException("A new typeConge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeConge result = typeCongeService.save(typeConge);
        return ResponseEntity
            .created(new URI("/api/type-conges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-conges/:id} : Updates an existing typeConge.
     *
     * @param id the id of the typeConge to save.
     * @param typeConge the typeConge to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeConge,
     * or with status {@code 400 (Bad Request)} if the typeConge is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeConge couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-conges/{id}")
    public ResponseEntity<TypeConge> updateTypeConge(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeConge typeConge
    ) throws URISyntaxException {
        log.debug("REST request to update TypeConge : {}, {}", id, typeConge);
        if (typeConge.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeConge.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeCongeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeConge result = typeCongeService.save(typeConge);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeConge.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-conges/:id} : Partial updates given fields of an existing typeConge, field will ignore if it is null
     *
     * @param id the id of the typeConge to save.
     * @param typeConge the typeConge to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeConge,
     * or with status {@code 400 (Bad Request)} if the typeConge is not valid,
     * or with status {@code 404 (Not Found)} if the typeConge is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeConge couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-conges/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TypeConge> partialUpdateTypeConge(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeConge typeConge
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeConge partially : {}, {}", id, typeConge);
        if (typeConge.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeConge.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeCongeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeConge> result = typeCongeService.partialUpdate(typeConge);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeConge.getId().toString())
        );
    }

    /**
     * {@code GET  /type-conges} : get all the typeConges.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeConges in body.
     */
    @GetMapping("/type-conges")
    public ResponseEntity<List<TypeConge>> getAllTypeConges(Pageable pageable) {
        log.debug("REST request to get a page of TypeConges");
        Page<TypeConge> page = typeCongeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-conges/:id} : get the "id" typeConge.
     *
     * @param id the id of the typeConge to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeConge, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-conges/{id}")
    public ResponseEntity<TypeConge> getTypeConge(@PathVariable Long id) {
        log.debug("REST request to get TypeConge : {}", id);
        Optional<TypeConge> typeConge = typeCongeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeConge);
    }

    /**
     * {@code DELETE  /type-conges/:id} : delete the "id" typeConge.
     *
     * @param id the id of the typeConge to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-conges/{id}")
    public ResponseEntity<Void> deleteTypeConge(@PathVariable Long id) {
        log.debug("REST request to delete TypeConge : {}", id);
        typeCongeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
