package com.projet.gestionconge.service;

import com.projet.gestionconge.domain.Poste;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Poste}.
 */
public interface PosteService {
    /**
     * Save a poste.
     *
     * @param poste the entity to save.
     * @return the persisted entity.
     */
    Poste save(Poste poste);

    /**
     * Partially updates a poste.
     *
     * @param poste the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Poste> partialUpdate(Poste poste);

    /**
     * Get all the postes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Poste> findAll(Pageable pageable);

    /**
     * Get the "id" poste.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Poste> findOne(Long id);

    /**
     * Delete the "id" poste.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
