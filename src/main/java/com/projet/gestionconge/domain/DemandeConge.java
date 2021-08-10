package com.projet.gestionconge.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projet.gestionconge.domain.enumeration.Etat;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DemandeConge.
 */
@Entity
@Table(name = "demande_conge")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DemandeConge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "duree")
    private Float duree;

    @Column(name = "raison")
    private String raison;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat")
    private Etat etat;

    @ManyToOne
    private TypeConge typeConge;

    @ManyToOne
    @JsonIgnoreProperties(value = { "demandeConges", "contrats", "departement", "poste" }, allowSetters = true)
    private Salarie salarie;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DemandeConge id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public DemandeConge dateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public DemandeConge dateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Float getDuree() {
        return this.duree;
    }

    public DemandeConge duree(Float duree) {
        this.duree = duree;
        return this;
    }

    public void setDuree(Float duree) {
        this.duree = duree;
    }

    public String getRaison() {
        return this.raison;
    }

    public DemandeConge raison(String raison) {
        this.raison = raison;
        return this;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public Etat getEtat() {
        return this.etat;
    }

    public DemandeConge etat(Etat etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public TypeConge getTypeConge() {
        return this.typeConge;
    }

    public DemandeConge typeConge(TypeConge typeConge) {
        this.setTypeConge(typeConge);
        return this;
    }

    public void setTypeConge(TypeConge typeConge) {
        this.typeConge = typeConge;
    }

    public Salarie getSalarie() {
        return this.salarie;
    }

    public DemandeConge salarie(Salarie salarie) {
        this.setSalarie(salarie);
        return this;
    }

    public void setSalarie(Salarie salarie) {
        this.salarie = salarie;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemandeConge)) {
            return false;
        }
        return id != null && id.equals(((DemandeConge) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemandeConge{" +
            "id=" + getId() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", duree=" + getDuree() +
            ", raison='" + getRaison() + "'" +
            ", etat='" + getEtat() + "'" +
            "}";
    }
}
