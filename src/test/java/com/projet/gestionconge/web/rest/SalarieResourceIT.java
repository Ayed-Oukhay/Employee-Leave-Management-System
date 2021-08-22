package com.projet.gestionconge.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.projet.gestionconge.IntegrationTest;
import com.projet.gestionconge.domain.Salarie;
import com.projet.gestionconge.domain.enumeration.Role;
import com.projet.gestionconge.repository.SalarieRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SalarieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SalarieResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_MANAGER = "AAAAAAAAAA";
    private static final String UPDATED_MANAGER = "BBBBBBBBBB";

    private static final Role DEFAULT_ROLE = Role.ROLE_ADMIN;
    private static final Role UPDATED_ROLE = Role.ROLE_USER;

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_I_D_ENTREPRISE = 1L;
    private static final Long UPDATED_I_D_ENTREPRISE = 2L;

    private static final String DEFAULT_L_DAP_PATH = "AAAAAAAAAA";
    private static final String UPDATED_L_DAP_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/salaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SalarieRepository salarieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalarieMockMvc;

    private Salarie salarie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Salarie createEntity(EntityManager em) {
        Salarie salarie = new Salarie()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .login(DEFAULT_LOGIN)
            .email(DEFAULT_EMAIL)
            .manager(DEFAULT_MANAGER)
            .role(DEFAULT_ROLE)
            .actif(DEFAULT_ACTIF)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .iDEntreprise(DEFAULT_I_D_ENTREPRISE)
            .lDAPPath(DEFAULT_L_DAP_PATH);
        return salarie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Salarie createUpdatedEntity(EntityManager em) {
        Salarie salarie = new Salarie()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .login(UPDATED_LOGIN)
            .email(UPDATED_EMAIL)
            .manager(UPDATED_MANAGER)
            .role(UPDATED_ROLE)
            .actif(UPDATED_ACTIF)
            .dateDebut(UPDATED_DATE_DEBUT)
            .iDEntreprise(UPDATED_I_D_ENTREPRISE)
            .lDAPPath(UPDATED_L_DAP_PATH);
        return salarie;
    }

    @BeforeEach
    public void initTest() {
        salarie = createEntity(em);
    }

    @Test
    @Transactional
    void createSalarie() throws Exception {
        int databaseSizeBeforeCreate = salarieRepository.findAll().size();
        // Create the Salarie
        restSalarieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salarie)))
            .andExpect(status().isCreated());

        // Validate the Salarie in the database
        List<Salarie> salarieList = salarieRepository.findAll();
        assertThat(salarieList).hasSize(databaseSizeBeforeCreate + 1);
        Salarie testSalarie = salarieList.get(salarieList.size() - 1);
        assertThat(testSalarie.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testSalarie.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testSalarie.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testSalarie.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSalarie.getManager()).isEqualTo(DEFAULT_MANAGER);
        assertThat(testSalarie.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testSalarie.getActif()).isEqualTo(DEFAULT_ACTIF);
        assertThat(testSalarie.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testSalarie.getiDEntreprise()).isEqualTo(DEFAULT_I_D_ENTREPRISE);
        assertThat(testSalarie.getlDAPPath()).isEqualTo(DEFAULT_L_DAP_PATH);
    }

    @Test
    @Transactional
    void createSalarieWithExistingId() throws Exception {
        // Create the Salarie with an existing ID
        salarie.setId(1L);

        int databaseSizeBeforeCreate = salarieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalarieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salarie)))
            .andExpect(status().isBadRequest());

        // Validate the Salarie in the database
        List<Salarie> salarieList = salarieRepository.findAll();
        assertThat(salarieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSalaries() throws Exception {
        // Initialize the database
        salarieRepository.saveAndFlush(salarie);

        // Get all the salarieList
        restSalarieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salarie.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].manager").value(hasItem(DEFAULT_MANAGER)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].iDEntreprise").value(hasItem(DEFAULT_I_D_ENTREPRISE.intValue())))
            .andExpect(jsonPath("$.[*].lDAPPath").value(hasItem(DEFAULT_L_DAP_PATH)));
    }

    @Test
    @Transactional
    void getSalarie() throws Exception {
        // Initialize the database
        salarieRepository.saveAndFlush(salarie);

        // Get the salarie
        restSalarieMockMvc
            .perform(get(ENTITY_API_URL_ID, salarie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salarie.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.manager").value(DEFAULT_MANAGER))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF.booleanValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.iDEntreprise").value(DEFAULT_I_D_ENTREPRISE.intValue()))
            .andExpect(jsonPath("$.lDAPPath").value(DEFAULT_L_DAP_PATH));
    }

    @Test
    @Transactional
    void getNonExistingSalarie() throws Exception {
        // Get the salarie
        restSalarieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSalarie() throws Exception {
        // Initialize the database
        salarieRepository.saveAndFlush(salarie);

        int databaseSizeBeforeUpdate = salarieRepository.findAll().size();

        // Update the salarie
        Salarie updatedSalarie = salarieRepository.findById(salarie.getId()).get();
        // Disconnect from session so that the updates on updatedSalarie are not directly saved in db
        em.detach(updatedSalarie);
        updatedSalarie
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .login(UPDATED_LOGIN)
            .email(UPDATED_EMAIL)
            .manager(UPDATED_MANAGER)
            .role(UPDATED_ROLE)
            .actif(UPDATED_ACTIF)
            .dateDebut(UPDATED_DATE_DEBUT)
            .iDEntreprise(UPDATED_I_D_ENTREPRISE)
            .lDAPPath(UPDATED_L_DAP_PATH);

        restSalarieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSalarie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSalarie))
            )
            .andExpect(status().isOk());

        // Validate the Salarie in the database
        List<Salarie> salarieList = salarieRepository.findAll();
        assertThat(salarieList).hasSize(databaseSizeBeforeUpdate);
        Salarie testSalarie = salarieList.get(salarieList.size() - 1);
        assertThat(testSalarie.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testSalarie.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testSalarie.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testSalarie.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSalarie.getManager()).isEqualTo(UPDATED_MANAGER);
        assertThat(testSalarie.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testSalarie.getActif()).isEqualTo(UPDATED_ACTIF);
        assertThat(testSalarie.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testSalarie.getiDEntreprise()).isEqualTo(UPDATED_I_D_ENTREPRISE);
        assertThat(testSalarie.getlDAPPath()).isEqualTo(UPDATED_L_DAP_PATH);
    }

    @Test
    @Transactional
    void putNonExistingSalarie() throws Exception {
        int databaseSizeBeforeUpdate = salarieRepository.findAll().size();
        salarie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalarieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salarie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salarie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salarie in the database
        List<Salarie> salarieList = salarieRepository.findAll();
        assertThat(salarieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSalarie() throws Exception {
        int databaseSizeBeforeUpdate = salarieRepository.findAll().size();
        salarie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalarieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salarie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salarie in the database
        List<Salarie> salarieList = salarieRepository.findAll();
        assertThat(salarieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSalarie() throws Exception {
        int databaseSizeBeforeUpdate = salarieRepository.findAll().size();
        salarie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalarieMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salarie)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Salarie in the database
        List<Salarie> salarieList = salarieRepository.findAll();
        assertThat(salarieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSalarieWithPatch() throws Exception {
        // Initialize the database
        salarieRepository.saveAndFlush(salarie);

        int databaseSizeBeforeUpdate = salarieRepository.findAll().size();

        // Update the salarie using partial update
        Salarie partialUpdatedSalarie = new Salarie();
        partialUpdatedSalarie.setId(salarie.getId());

        partialUpdatedSalarie
            .login(UPDATED_LOGIN)
            .role(UPDATED_ROLE)
            .actif(UPDATED_ACTIF)
            .iDEntreprise(UPDATED_I_D_ENTREPRISE)
            .lDAPPath(UPDATED_L_DAP_PATH);

        restSalarieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalarie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalarie))
            )
            .andExpect(status().isOk());

        // Validate the Salarie in the database
        List<Salarie> salarieList = salarieRepository.findAll();
        assertThat(salarieList).hasSize(databaseSizeBeforeUpdate);
        Salarie testSalarie = salarieList.get(salarieList.size() - 1);
        assertThat(testSalarie.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testSalarie.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testSalarie.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testSalarie.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSalarie.getManager()).isEqualTo(DEFAULT_MANAGER);
        assertThat(testSalarie.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testSalarie.getActif()).isEqualTo(UPDATED_ACTIF);
        assertThat(testSalarie.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testSalarie.getiDEntreprise()).isEqualTo(UPDATED_I_D_ENTREPRISE);
        assertThat(testSalarie.getlDAPPath()).isEqualTo(UPDATED_L_DAP_PATH);
    }

    @Test
    @Transactional
    void fullUpdateSalarieWithPatch() throws Exception {
        // Initialize the database
        salarieRepository.saveAndFlush(salarie);

        int databaseSizeBeforeUpdate = salarieRepository.findAll().size();

        // Update the salarie using partial update
        Salarie partialUpdatedSalarie = new Salarie();
        partialUpdatedSalarie.setId(salarie.getId());

        partialUpdatedSalarie
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .login(UPDATED_LOGIN)
            .email(UPDATED_EMAIL)
            .manager(UPDATED_MANAGER)
            .role(UPDATED_ROLE)
            .actif(UPDATED_ACTIF)
            .dateDebut(UPDATED_DATE_DEBUT)
            .iDEntreprise(UPDATED_I_D_ENTREPRISE)
            .lDAPPath(UPDATED_L_DAP_PATH);

        restSalarieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalarie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalarie))
            )
            .andExpect(status().isOk());

        // Validate the Salarie in the database
        List<Salarie> salarieList = salarieRepository.findAll();
        assertThat(salarieList).hasSize(databaseSizeBeforeUpdate);
        Salarie testSalarie = salarieList.get(salarieList.size() - 1);
        assertThat(testSalarie.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testSalarie.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testSalarie.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testSalarie.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSalarie.getManager()).isEqualTo(UPDATED_MANAGER);
        assertThat(testSalarie.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testSalarie.getActif()).isEqualTo(UPDATED_ACTIF);
        assertThat(testSalarie.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testSalarie.getiDEntreprise()).isEqualTo(UPDATED_I_D_ENTREPRISE);
        assertThat(testSalarie.getlDAPPath()).isEqualTo(UPDATED_L_DAP_PATH);
    }

    @Test
    @Transactional
    void patchNonExistingSalarie() throws Exception {
        int databaseSizeBeforeUpdate = salarieRepository.findAll().size();
        salarie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalarieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, salarie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salarie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salarie in the database
        List<Salarie> salarieList = salarieRepository.findAll();
        assertThat(salarieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSalarie() throws Exception {
        int databaseSizeBeforeUpdate = salarieRepository.findAll().size();
        salarie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalarieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salarie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salarie in the database
        List<Salarie> salarieList = salarieRepository.findAll();
        assertThat(salarieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSalarie() throws Exception {
        int databaseSizeBeforeUpdate = salarieRepository.findAll().size();
        salarie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalarieMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(salarie)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Salarie in the database
        List<Salarie> salarieList = salarieRepository.findAll();
        assertThat(salarieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSalarie() throws Exception {
        // Initialize the database
        salarieRepository.saveAndFlush(salarie);

        int databaseSizeBeforeDelete = salarieRepository.findAll().size();

        // Delete the salarie
        restSalarieMockMvc
            .perform(delete(ENTITY_API_URL_ID, salarie.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Salarie> salarieList = salarieRepository.findAll();
        assertThat(salarieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
