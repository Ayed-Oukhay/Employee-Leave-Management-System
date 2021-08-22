package com.projet.gestionconge.repository;

import com.projet.gestionconge.domain.TypeConge;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TypeConge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeCongeRepository extends JpaRepository<TypeConge, Long> {}
