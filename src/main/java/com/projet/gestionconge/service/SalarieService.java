package com.projet.gestionconge.service;

import com.projet.gestionconge.domain.Salarie;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Salarie}.
 */
public interface SalarieService {
    /**
     * Save a salarie.
     *
     * @param salarie the entity to save.
     * @return the persisted entity.
     */
    Salarie save(Salarie salarie);

    /**
     * Partially updates a salarie.
     *
     * @param salarie the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Salarie> partialUpdate(Salarie salarie);

    /**
     * Get all the salaries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Salarie> findAll(Pageable pageable);

    /**
     * Get the "id" salarie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Salarie> findOne(Long id);

    /**
     * Delete the "id" salarie.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
