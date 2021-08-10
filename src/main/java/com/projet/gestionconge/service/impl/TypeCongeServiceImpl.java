package com.projet.gestionconge.service.impl;

import com.projet.gestionconge.domain.TypeConge;
import com.projet.gestionconge.repository.TypeCongeRepository;
import com.projet.gestionconge.service.TypeCongeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TypeConge}.
 */
@Service
@Transactional
public class TypeCongeServiceImpl implements TypeCongeService {

    private final Logger log = LoggerFactory.getLogger(TypeCongeServiceImpl.class);

    private final TypeCongeRepository typeCongeRepository;

    public TypeCongeServiceImpl(TypeCongeRepository typeCongeRepository) {
        this.typeCongeRepository = typeCongeRepository;
    }

    @Override
    public TypeConge save(TypeConge typeConge) {
        log.debug("Request to save TypeConge : {}", typeConge);
        return typeCongeRepository.save(typeConge);
    }

    @Override
    public Optional<TypeConge> partialUpdate(TypeConge typeConge) {
        log.debug("Request to partially update TypeConge : {}", typeConge);

        return typeCongeRepository
            .findById(typeConge.getId())
            .map(
                existingTypeConge -> {
                    if (typeConge.getNom() != null) {
                        existingTypeConge.setNom(typeConge.getNom());
                    }

                    return existingTypeConge;
                }
            )
            .map(typeCongeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeConge> findAll(Pageable pageable) {
        log.debug("Request to get all TypeConges");
        return typeCongeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeConge> findOne(Long id) {
        log.debug("Request to get TypeConge : {}", id);
        return typeCongeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeConge : {}", id);
        typeCongeRepository.deleteById(id);
    }
}
