package com.projet.gestionconge.service;

import com.projet.gestionconge.domain.TypeContrat;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TypeContrat}.
 */
public interface TypeContratService {
    /**
     * Save a typeContrat.
     *
     * @param typeContrat the entity to save.
     * @return the persisted entity.
     */
    TypeContrat save(TypeContrat typeContrat);

    /**
     * Partially updates a typeContrat.
     *
     * @param typeContrat the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypeContrat> partialUpdate(TypeContrat typeContrat);

    /**
     * Get all the typeContrats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeContrat> findAll(Pageable pageable);

    /**
     * Get the "id" typeContrat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeContrat> findOne(Long id);

    /**
     * Delete the "id" typeContrat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
