package com.projet.gestionconge.service.impl;

import com.projet.gestionconge.domain.Poste;
import com.projet.gestionconge.repository.PosteRepository;
import com.projet.gestionconge.service.PosteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Poste}.
 */
@Service
@Transactional
public class PosteServiceImpl implements PosteService {

    private final Logger log = LoggerFactory.getLogger(PosteServiceImpl.class);

    private final PosteRepository posteRepository;

    public PosteServiceImpl(PosteRepository posteRepository) {
        this.posteRepository = posteRepository;
    }

    @Override
    public Poste save(Poste poste) {
        log.debug("Request to save Poste : {}", poste);
        return posteRepository.save(poste);
    }

    @Override
    public Optional<Poste> partialUpdate(Poste poste) {
        log.debug("Request to partially update Poste : {}", poste);

        return posteRepository
            .findById(poste.getId())
            .map(
                existingPoste -> {
                    if (poste.getNom() != null) {
                        existingPoste.setNom(poste.getNom());
                    }
                    if (poste.getDescription() != null) {
                        existingPoste.setDescription(poste.getDescription());
                    }

                    return existingPoste;
                }
            )
            .map(posteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Poste> findAll(Pageable pageable) {
        log.debug("Request to get all Postes");
        return posteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Poste> findOne(Long id) {
        log.debug("Request to get Poste : {}", id);
        return posteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Poste : {}", id);
        posteRepository.deleteById(id);
    }
}
