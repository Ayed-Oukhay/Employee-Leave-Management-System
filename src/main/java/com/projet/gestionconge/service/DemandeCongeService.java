package com.projet.gestionconge.service;

import com.projet.gestionconge.domain.DemandeConge;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link DemandeConge}.
 */
public interface DemandeCongeService {
    /**
     * Save a demandeConge.
     *
     * @param demandeConge the entity to save.
     * @return the persisted entity.
     */
    DemandeConge save(DemandeConge demandeConge);

    /**
     * Partially updates a demandeConge.
     *
     * @param demandeConge the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DemandeConge> partialUpdate(DemandeConge demandeConge);

    /**
     * Get all the demandeConges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DemandeConge> findAll(Pageable pageable);

    /**
     * Get the "id" demandeConge.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DemandeConge> findOne(Long id);

    /**
     * Delete the "id" demandeConge.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the demandeConges based on the connected manager.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    /*Page<DemandeConge> findByManager(Pageable pageable);*/

}
