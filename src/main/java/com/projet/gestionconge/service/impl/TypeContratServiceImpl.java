package com.projet.gestionconge.service.impl;

import com.projet.gestionconge.domain.TypeContrat;
import com.projet.gestionconge.repository.TypeContratRepository;
import com.projet.gestionconge.service.TypeContratService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TypeContrat}.
 */
@Service
@Transactional
public class TypeContratServiceImpl implements TypeContratService {

    private final Logger log = LoggerFactory.getLogger(TypeContratServiceImpl.class);

    private final TypeContratRepository typeContratRepository;

    public TypeContratServiceImpl(TypeContratRepository typeContratRepository) {
        this.typeContratRepository = typeContratRepository;
    }

    @Override
    public TypeContrat save(TypeContrat typeContrat) {
        log.debug("Request to save TypeContrat : {}", typeContrat);
        return typeContratRepository.save(typeContrat);
    }

    @Override
    public Optional<TypeContrat> partialUpdate(TypeContrat typeContrat) {
        log.debug("Request to partially update TypeContrat : {}", typeContrat);

        return typeContratRepository
            .findById(typeContrat.getId())
            .map(
                existingTypeContrat -> {
                    if (typeContrat.getNom() != null) {
                        existingTypeContrat.setNom(typeContrat.getNom());
                    }

                    return existingTypeContrat;
                }
            )
            .map(typeContratRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeContrat> findAll(Pageable pageable) {
        log.debug("Request to get all TypeContrats");
        return typeContratRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeContrat> findOne(Long id) {
        log.debug("Request to get TypeContrat : {}", id);
        return typeContratRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeContrat : {}", id);
        typeContratRepository.deleteById(id);
    }
}
