package com.projet.gestionconge.repository;

import com.projet.gestionconge.domain.Salarie;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Salarie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalarieRepository extends JpaRepository<Salarie, Long> {}
