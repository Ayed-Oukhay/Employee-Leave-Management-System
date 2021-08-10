package com.projet.gestionconge.repository;

import com.projet.gestionconge.domain.DemandeConge;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DemandeConge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DemandeCongeRepository extends JpaRepository<DemandeConge, Long> {}
