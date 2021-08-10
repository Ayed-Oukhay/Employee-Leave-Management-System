package com.projet.gestionconge.service;

import com.projet.gestionconge.domain.Contrat;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Contrat}.
 */
public interface ContratService {
    /**
     * Save a contrat.
     *
     * @param contrat the entity to save.
     * @return the persisted entity.
     */
    Contrat save(Contrat contrat);

    /**
     * Partially updates a contrat.
     *
     * @param contrat the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Contrat> partialUpdate(Contrat contrat);

    /**
     * Get all the contrats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Contrat> findAll(Pageable pageable);

    /**
     * Get the "id" contrat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Contrat> findOne(Long id);

    /**
     * Delete the "id" contrat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
