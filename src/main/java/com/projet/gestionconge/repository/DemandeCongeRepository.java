package com.projet.gestionconge.repository;

import com.projet.gestionconge.domain.DemandeConge;
import com.projet.gestionconge.domain.Salarie;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.*;

/**
 * Spring Data SQL repository for the DemandeConge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DemandeCongeRepository extends JpaRepository<DemandeConge, Long> {
    /* @Query("SELECT d FROM DemandeConge d WHERE d.salarie_id = 2")
    Page<DemandeConge> findByManager(Pageable pageable); */
}
