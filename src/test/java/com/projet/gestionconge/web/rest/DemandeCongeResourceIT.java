package com.projet.gestionconge.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.projet.gestionconge.IntegrationTest;
import com.projet.gestionconge.domain.DemandeConge;
import com.projet.gestionconge.domain.enumeration.Etat;
import com.projet.gestionconge.repository.DemandeCongeRepository;
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
 * Integration tests for the {@link DemandeCongeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DemandeCongeResourceIT {

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_DUREE = 1F;
    private static final Float UPDATED_DUREE = 2F;

    private static final String DEFAULT_RAISON = "AAAAAAAAAA";
    private static final String UPDATED_RAISON = "BBBBBBBBBB";

    private static final Etat DEFAULT_ETAT = Etat.Accepte;
    private static final Etat UPDATED_ETAT = Etat.Refuse;

    private static final String ENTITY_API_URL = "/api/demande-conges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DemandeCongeRepository demandeCongeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDemandeCongeMockMvc;

    private DemandeConge demandeConge;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandeConge createEntity(EntityManager em) {
        DemandeConge demandeConge = new DemandeConge()
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .duree(DEFAULT_DUREE)
            .raison(DEFAULT_RAISON)
            .etat(DEFAULT_ETAT);
        return demandeConge;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandeConge createUpdatedEntity(EntityManager em) {
        DemandeConge demandeConge = new DemandeConge()
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .duree(UPDATED_DUREE)
            .raison(UPDATED_RAISON)
            .etat(UPDATED_ETAT);
        return demandeConge;
    }

    @BeforeEach
    public void initTest() {
        demandeConge = createEntity(em);
    }

    @Test
    @Transactional
    void createDemandeConge() throws Exception {
        int databaseSizeBeforeCreate = demandeCongeRepository.findAll().size();
        // Create the DemandeConge
        restDemandeCongeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeConge)))
            .andExpect(status().isCreated());

        // Validate the DemandeConge in the database
        List<DemandeConge> demandeCongeList = demandeCongeRepository.findAll();
        assertThat(demandeCongeList).hasSize(databaseSizeBeforeCreate + 1);
        DemandeConge testDemandeConge = demandeCongeList.get(demandeCongeList.size() - 1);
        assertThat(testDemandeConge.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testDemandeConge.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testDemandeConge.getDuree()).isEqualTo(DEFAULT_DUREE);
        assertThat(testDemandeConge.getRaison()).isEqualTo(DEFAULT_RAISON);
        assertThat(testDemandeConge.getEtat()).isEqualTo(DEFAULT_ETAT);
    }

    @Test
    @Transactional
    void createDemandeCongeWithExistingId() throws Exception {
        // Create the DemandeConge with an existing ID
        demandeConge.setId(1L);

        int databaseSizeBeforeCreate = demandeCongeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemandeCongeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeConge)))
            .andExpect(status().isBadRequest());

        // Validate the DemandeConge in the database
        List<DemandeConge> demandeCongeList = demandeCongeRepository.findAll();
        assertThat(demandeCongeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDemandeConges() throws Exception {
        // Initialize the database
        demandeCongeRepository.saveAndFlush(demandeConge);

        // Get all the demandeCongeList
        restDemandeCongeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demandeConge.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].duree").value(hasItem(DEFAULT_DUREE.doubleValue())))
            .andExpect(jsonPath("$.[*].raison").value(hasItem(DEFAULT_RAISON)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));
    }

    @Test
    @Transactional
    void getDemandeConge() throws Exception {
        // Initialize the database
        demandeCongeRepository.saveAndFlush(demandeConge);

        // Get the demandeConge
        restDemandeCongeMockMvc
            .perform(get(ENTITY_API_URL_ID, demandeConge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(demandeConge.getId().intValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.duree").value(DEFAULT_DUREE.doubleValue()))
            .andExpect(jsonPath("$.raison").value(DEFAULT_RAISON))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDemandeConge() throws Exception {
        // Get the demandeConge
        restDemandeCongeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDemandeConge() throws Exception {
        // Initialize the database
        demandeCongeRepository.saveAndFlush(demandeConge);

        int databaseSizeBeforeUpdate = demandeCongeRepository.findAll().size();

        // Update the demandeConge
        DemandeConge updatedDemandeConge = demandeCongeRepository.findById(demandeConge.getId()).get();
        // Disconnect from session so that the updates on updatedDemandeConge are not directly saved in db
        em.detach(updatedDemandeConge);
        updatedDemandeConge
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .duree(UPDATED_DUREE)
            .raison(UPDATED_RAISON)
            .etat(UPDATED_ETAT);

        restDemandeCongeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDemandeConge.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDemandeConge))
            )
            .andExpect(status().isOk());

        // Validate the DemandeConge in the database
        List<DemandeConge> demandeCongeList = demandeCongeRepository.findAll();
        assertThat(demandeCongeList).hasSize(databaseSizeBeforeUpdate);
        DemandeConge testDemandeConge = demandeCongeList.get(demandeCongeList.size() - 1);
        assertThat(testDemandeConge.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testDemandeConge.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testDemandeConge.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testDemandeConge.getRaison()).isEqualTo(UPDATED_RAISON);
        assertThat(testDemandeConge.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    @Transactional
    void putNonExistingDemandeConge() throws Exception {
        int databaseSizeBeforeUpdate = demandeCongeRepository.findAll().size();
        demandeConge.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeCongeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demandeConge.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeConge))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeConge in the database
        List<DemandeConge> demandeCongeList = demandeCongeRepository.findAll();
        assertThat(demandeCongeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDemandeConge() throws Exception {
        int databaseSizeBeforeUpdate = demandeCongeRepository.findAll().size();
        demandeConge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeCongeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeConge))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeConge in the database
        List<DemandeConge> demandeCongeList = demandeCongeRepository.findAll();
        assertThat(demandeCongeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDemandeConge() throws Exception {
        int databaseSizeBeforeUpdate = demandeCongeRepository.findAll().size();
        demandeConge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeCongeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeConge)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandeConge in the database
        List<DemandeConge> demandeCongeList = demandeCongeRepository.findAll();
        assertThat(demandeCongeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDemandeCongeWithPatch() throws Exception {
        // Initialize the database
        demandeCongeRepository.saveAndFlush(demandeConge);

        int databaseSizeBeforeUpdate = demandeCongeRepository.findAll().size();

        // Update the demandeConge using partial update
        DemandeConge partialUpdatedDemandeConge = new DemandeConge();
        partialUpdatedDemandeConge.setId(demandeConge.getId());

        partialUpdatedDemandeConge.duree(UPDATED_DUREE).etat(UPDATED_ETAT);

        restDemandeCongeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandeConge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandeConge))
            )
            .andExpect(status().isOk());

        // Validate the DemandeConge in the database
        List<DemandeConge> demandeCongeList = demandeCongeRepository.findAll();
        assertThat(demandeCongeList).hasSize(databaseSizeBeforeUpdate);
        DemandeConge testDemandeConge = demandeCongeList.get(demandeCongeList.size() - 1);
        assertThat(testDemandeConge.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testDemandeConge.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testDemandeConge.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testDemandeConge.getRaison()).isEqualTo(DEFAULT_RAISON);
        assertThat(testDemandeConge.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    @Transactional
    void fullUpdateDemandeCongeWithPatch() throws Exception {
        // Initialize the database
        demandeCongeRepository.saveAndFlush(demandeConge);

        int databaseSizeBeforeUpdate = demandeCongeRepository.findAll().size();

        // Update the demandeConge using partial update
        DemandeConge partialUpdatedDemandeConge = new DemandeConge();
        partialUpdatedDemandeConge.setId(demandeConge.getId());

        partialUpdatedDemandeConge
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .duree(UPDATED_DUREE)
            .raison(UPDATED_RAISON)
            .etat(UPDATED_ETAT);

        restDemandeCongeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandeConge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandeConge))
            )
            .andExpect(status().isOk());

        // Validate the DemandeConge in the database
        List<DemandeConge> demandeCongeList = demandeCongeRepository.findAll();
        assertThat(demandeCongeList).hasSize(databaseSizeBeforeUpdate);
        DemandeConge testDemandeConge = demandeCongeList.get(demandeCongeList.size() - 1);
        assertThat(testDemandeConge.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testDemandeConge.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testDemandeConge.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testDemandeConge.getRaison()).isEqualTo(UPDATED_RAISON);
        assertThat(testDemandeConge.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    @Transactional
    void patchNonExistingDemandeConge() throws Exception {
        int databaseSizeBeforeUpdate = demandeCongeRepository.findAll().size();
        demandeConge.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeCongeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, demandeConge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeConge))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeConge in the database
        List<DemandeConge> demandeCongeList = demandeCongeRepository.findAll();
        assertThat(demandeCongeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDemandeConge() throws Exception {
        int databaseSizeBeforeUpdate = demandeCongeRepository.findAll().size();
        demandeConge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeCongeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeConge))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeConge in the database
        List<DemandeConge> demandeCongeList = demandeCongeRepository.findAll();
        assertThat(demandeCongeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDemandeConge() throws Exception {
        int databaseSizeBeforeUpdate = demandeCongeRepository.findAll().size();
        demandeConge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeCongeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(demandeConge))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandeConge in the database
        List<DemandeConge> demandeCongeList = demandeCongeRepository.findAll();
        assertThat(demandeCongeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDemandeConge() throws Exception {
        // Initialize the database
        demandeCongeRepository.saveAndFlush(demandeConge);

        int databaseSizeBeforeDelete = demandeCongeRepository.findAll().size();

        // Delete the demandeConge
        restDemandeCongeMockMvc
            .perform(delete(ENTITY_API_URL_ID, demandeConge.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DemandeConge> demandeCongeList = demandeCongeRepository.findAll();
        assertThat(demandeCongeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
