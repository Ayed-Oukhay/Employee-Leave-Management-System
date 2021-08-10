package com.projet.gestionconge.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projet.gestionconge.domain.enumeration.Role;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Salarie.
 */
@Entity
@Table(name = "salarie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Salarie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "login")
    private String login;

    @Column(name = "email")
    private String email;

    @Column(name = "manager")
    private String manager;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "actif")
    private Boolean actif;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "i_d_entreprise")
    private Long iDEntreprise;

    @Column(name = "l_dap_path")
    private String lDAPPath;

    @OneToMany(mappedBy = "salarie")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "typeConge", "salarie" }, allowSetters = true)
    private Set<DemandeConge> demandeConges = new HashSet<>();

    @OneToMany(mappedBy = "salarie")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "typeContrat", "salarie" }, allowSetters = true)
    private Set<Contrat> contrats = new HashSet<>();

    @ManyToOne
    private Departement departement;

    @ManyToOne
    private Poste poste;

    @OneToOne
    @JoinColumn(name="id_user", referencedColumnName = "id")
    private User user;

    @OneToOne
    @JoinColumn(name="authority", referencedColumnName = "name")
    private Authority auth;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Salarie id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public Salarie nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Salarie prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getLogin() {
        return this.login;
    }

    public Salarie login(String login) {
        this.login = login;
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return this.email;
    }

    public Salarie email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getManager() {
        return this.manager;
    }

    public Salarie manager(String manager) {
        this.manager = manager;
        return this;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Role getRole() {
        return this.role;
    }

    public Salarie role(Role role) {
        this.role = role;
        return this;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public Salarie actif(Boolean actif) {
        this.actif = actif;
        return this;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public Salarie dateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Long getiDEntreprise() {
        return this.iDEntreprise;
    }

    public Salarie iDEntreprise(Long iDEntreprise) {
        this.iDEntreprise = iDEntreprise;
        return this;
    }

    public void setiDEntreprise(Long iDEntreprise) {
        this.iDEntreprise = iDEntreprise;
    }

    public String getlDAPPath() {
        return this.lDAPPath;
    }

    public Salarie lDAPPath(String lDAPPath) {
        this.lDAPPath = lDAPPath;
        return this;
    }

    public void setlDAPPath(String lDAPPath) {
        this.lDAPPath = lDAPPath;
    }

    public Set<DemandeConge> getDemandeConges() {
        return this.demandeConges;
    }

    public Salarie demandeConges(Set<DemandeConge> demandeConges) {
        this.setDemandeConges(demandeConges);
        return this;
    }

    public Salarie addDemandeConge(DemandeConge demandeConge) {
        this.demandeConges.add(demandeConge);
        demandeConge.setSalarie(this);
        return this;
    }

    public Salarie removeDemandeConge(DemandeConge demandeConge) {
        this.demandeConges.remove(demandeConge);
        demandeConge.setSalarie(null);
        return this;
    }

    public void setDemandeConges(Set<DemandeConge> demandeConges) {
        if (this.demandeConges != null) {
            this.demandeConges.forEach(i -> i.setSalarie(null));
        }
        if (demandeConges != null) {
            demandeConges.forEach(i -> i.setSalarie(this));
        }
        this.demandeConges = demandeConges;
    }

    public Set<Contrat> getContrats() {
        return this.contrats;
    }

    public Salarie contrats(Set<Contrat> contrats) {
        this.setContrats(contrats);
        return this;
    }

    public Salarie addContrat(Contrat contrat) {
        this.contrats.add(contrat);
        contrat.setSalarie(this);
        return this;
    }

    public Salarie removeContrat(Contrat contrat) {
        this.contrats.remove(contrat);
        contrat.setSalarie(null);
        return this;
    }

    public void setContrats(Set<Contrat> contrats) {
        if (this.contrats != null) {
            this.contrats.forEach(i -> i.setSalarie(null));
        }
        if (contrats != null) {
            contrats.forEach(i -> i.setSalarie(this));
        }
        this.contrats = contrats;
    }

    public Departement getDepartement() {
        return this.departement;
    }

    public Salarie departement(Departement departement) {
        this.setDepartement(departement);
        return this;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Poste getPoste() {
        return this.poste;
    }

    public Salarie poste(Poste poste) {
        this.setPoste(poste);
        return this;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void setAuth(Authority auth){
        this.auth = auth;
    }

    public Authority getAuth() {
        return this.auth;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Salarie)) {
            return false;
        }
        return id != null && id.equals(((Salarie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Salarie{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", login='" + getLogin() + "'" +
            ", email='" + getEmail() + "'" +
            ", manager='" + getManager() + "'" +
            ", role='" + getRole() + "'" +
            ", actif='" + getActif() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", iDEntreprise=" + getiDEntreprise() +
            ", lDAPPath='" + getlDAPPath() + "'" +
            "}";
    }
}
