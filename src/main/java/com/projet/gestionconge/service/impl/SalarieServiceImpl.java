package com.projet.gestionconge.service.impl;

import com.projet.gestionconge.domain.Authority;
import com.projet.gestionconge.domain.Salarie;
import com.projet.gestionconge.domain.User;
import com.projet.gestionconge.repository.AuthorityRepository;
import com.projet.gestionconge.repository.SalarieRepository;
import com.projet.gestionconge.repository.UserRepository;
import com.projet.gestionconge.security.AuthoritiesConstants;
import com.projet.gestionconge.service.SalarieService;
import java.time.Instant;
import java.util.*;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;

/**
 * Service Implementation for managing {@link Salarie}.
 */
@Service
@Transactional
public class SalarieServiceImpl implements SalarieService {

    private final Logger log = LoggerFactory.getLogger(SalarieServiceImpl.class);

    private final SalarieRepository salarieRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    public SalarieServiceImpl(
        SalarieRepository salarieRepository,
        UserRepository userRepository,
        AuthorityRepository authorityRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.salarieRepository = salarieRepository;
        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;

        this.authorityRepository = authorityRepository;
    }

    @Override
    public Salarie save(Salarie salarie) {
        log.debug("Request to save Salarie : {}", salarie);
        /* Création d'un nouveau user au même temps */

        User user = new User();

        user.setLogin(salarie.getLogin().toLowerCase());

        user.setFirstName(salarie.getPrenom());

        user.setLastName(salarie.getNom());

        user.setEmail(salarie.getEmail().toLowerCase());

        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());

        user.setPassword(encryptedPassword);

        user.setResetKey(RandomUtil.generateResetKey());

        user.setResetDate(Instant.now());

        user.setActivated(true);

        Set<Authority> authorities = new HashSet<>();

        if (salarie.getRole() == salarie.getRole().ROLE_ADMIN) {
            authorityRepository.findById(AuthoritiesConstants.ADMIN).ifPresent(authorities::add);
        } else {
            authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        }

        user.setAuthorities(authorities);

        User savedUser = userRepository.save(user);

        /* ------------------- */

        salarie.setUser(savedUser);
        return salarieRepository.save(salarie);
    }

    @Override
    public Optional<Salarie> partialUpdate(Salarie salarie) {
        log.debug("Request to partially update Salarie : {}", salarie);

        return salarieRepository
            .findById(salarie.getId())
            .map(
                existingSalarie -> {
                    if (salarie.getNom() != null) {
                        existingSalarie.setNom(salarie.getNom());
                    }
                    if (salarie.getPrenom() != null) {
                        existingSalarie.setPrenom(salarie.getPrenom());
                    }
                    if (salarie.getLogin() != null) {
                        existingSalarie.setLogin(salarie.getLogin());
                    }
                    if (salarie.getEmail() != null) {
                        existingSalarie.setEmail(salarie.getEmail());
                    }
                    if (salarie.getManager() != null) {
                        existingSalarie.setManager(salarie.getManager());
                    }
                    if (salarie.getRole() != null) {
                        existingSalarie.setRole(salarie.getRole());
                    }
                    if (salarie.getActif() != null) {
                        existingSalarie.setActif(salarie.getActif());
                    }
                    if (salarie.getDateDebut() != null) {
                        existingSalarie.setDateDebut(salarie.getDateDebut());
                    }
                    if (salarie.getiDEntreprise() != null) {
                        existingSalarie.setiDEntreprise(salarie.getiDEntreprise());
                    }
                    if (salarie.getlDAPPath() != null) {
                        existingSalarie.setlDAPPath(salarie.getlDAPPath());
                    }

                    return existingSalarie;
                }
            )
            .map(salarieRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Salarie> findAll(Pageable pageable) {
        log.debug("Request to get all Salaries");
        return salarieRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Salarie> findOne(Long id) {
        log.debug("Request to get Salarie : {}", id);
        return salarieRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Salarie : {}", id);
        salarieRepository.deleteById(id);
    }
}
