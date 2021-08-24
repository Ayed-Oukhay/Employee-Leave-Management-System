package com.projet.gestionconge.service.impl;

import com.projet.gestionconge.domain.Salarie;
import com.projet.gestionconge.domain.DemandeConge;
import com.projet.gestionconge.domain.User;
import com.projet.gestionconge.repository.DemandeCongeRepository;
import com.projet.gestionconge.service.DemandeCongeService;
import com.projet.gestionconge.repository.UserRepository;
import com.projet.gestionconge.repository.SalarieRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * Service Implementation for managing {@link DemandeConge}.
 */
@Service
@Transactional
public class DemandeCongeServiceImpl implements DemandeCongeService {

    private final Logger log = LoggerFactory.getLogger(DemandeCongeServiceImpl.class);

    private final DemandeCongeRepository demandeCongeRepository;

    private final UserRepository userRepository;

    private final SalarieRepository salarieRepository;

    public DemandeCongeServiceImpl(DemandeCongeRepository demandeCongeRepository, UserRepository userRepository, SalarieRepository salarieRepository) {
        this.demandeCongeRepository = demandeCongeRepository;
        this.userRepository = userRepository;
        this.salarieRepository = salarieRepository;
    }

    @Override
    public DemandeConge save(DemandeConge demandeConge) {
        log.debug("Request to save DemandeConge : {}", demandeConge);
        return demandeCongeRepository.save(demandeConge);
    }

    @Override
    public Optional<DemandeConge> partialUpdate(DemandeConge demandeConge) {
        log.debug("Request to partially update DemandeConge : {}", demandeConge);

        return demandeCongeRepository
            .findById(demandeConge.getId())
            .map(
                existingDemandeConge -> {
                    if (demandeConge.getDateDebut() != null) {
                        existingDemandeConge.setDateDebut(demandeConge.getDateDebut());
                    }
                    if (demandeConge.getDateFin() != null) {
                        existingDemandeConge.setDateFin(demandeConge.getDateFin());
                    }
                    if (demandeConge.getDuree() != null) {
                        existingDemandeConge.setDuree(demandeConge.getDuree());
                    }
                    if (demandeConge.getRaison() != null) {
                        existingDemandeConge.setRaison(demandeConge.getRaison());
                    }
                    if (demandeConge.getEtat() != null) {
                        existingDemandeConge.setEtat(demandeConge.getEtat());
                    }

                    return existingDemandeConge;
                }
            )
            .map(demandeCongeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DemandeConge> findAll(Pageable pageable) {
        log.debug("Request to get all DemandeConges");
        return demandeCongeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DemandeConge> findOne(Long id) {
        log.debug("Request to get DemandeConge : {}", id);
        return demandeCongeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DemandeConge : {}", id);
        demandeCongeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DemandeConge> findByManager(Pageable pageable) {
        log.debug("Request to get DemandeConges based on the connected HR Admin");
         List<DemandeConge> filteredRepo = demandeCongeRepository.findAll(pageable);
        for (DemandeConge d:demandeCongeRepository.findAll(pageable)){
            Long idS = d.getSalarie().getId();
            Salarie s = salarieRepository.findOne(idS);
            String manager = s.getManager();
            if (manager=="admin"){
                filteredRepo.add(d);
            } 
        }
        /*-----------------------------------------*/
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        String CurrentUser = auth.getLogin();
        
        
        /* List<DemandeConge> filteredRepo = new List<DemandeConge>();
        for (Object d:DemandeCongeRepository.findAll()){
            if (d.getSalarie()==2){
                filteredRepo.add(d);
            }
        } 
        return filteredRepo.findAll(pageable);
        return demandeCongeRepository.findByManager(pageable);
    }
}
