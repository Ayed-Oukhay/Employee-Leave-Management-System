package com.projet.gestionconge.service;

import com.projet.gestionconge.domain.TypeConge;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TypeConge}.
 */
public interface TypeCongeService {
    /**
     * Save a typeConge.
     *
     * @param typeConge the entity to save.
     * @return the persisted entity.
     */
    TypeConge save(TypeConge typeConge);

    /**
     * Partially updates a typeConge.
     *
     * @param typeConge the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypeConge> partialUpdate(TypeConge typeConge);

    /**
     * Get all the typeConges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeConge> findAll(Pageable pageable);

    /**
     * Get the "id" typeConge.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeConge> findOne(Long id);

    /**
     * Delete the "id" typeConge.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
