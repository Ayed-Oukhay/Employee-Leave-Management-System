package com.projet.gestionconge.service.impl;

import com.projet.gestionconge.domain.Contrat;
import com.projet.gestionconge.repository.ContratRepository;
import com.projet.gestionconge.service.ContratService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Contrat}.
 */
@Service
@Transactional
public class ContratServiceImpl implements ContratService {

    private final Logger log = LoggerFactory.getLogger(ContratServiceImpl.class);

    private final ContratRepository contratRepository;

    public ContratServiceImpl(ContratRepository contratRepository) {
        this.contratRepository = contratRepository;
    }

    @Override
    public Contrat save(Contrat contrat) {
        log.debug("Request to save Contrat : {}", contrat);
        return contratRepository.save(contrat);
    }

    @Override
    public Optional<Contrat> partialUpdate(Contrat contrat) {
        log.debug("Request to partially update Contrat : {}", contrat);

        return contratRepository
            .findById(contrat.getId())
            .map(
                existingContrat -> {
                    return existingContrat;
                }
            )
            .map(contratRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Contrat> findAll(Pageable pageable) {
        log.debug("Request to get all Contrats");
        return contratRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Contrat> findOne(Long id) {
        log.debug("Request to get Contrat : {}", id);
        return contratRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Contrat : {}", id);
        contratRepository.deleteById(id);
    }
}
