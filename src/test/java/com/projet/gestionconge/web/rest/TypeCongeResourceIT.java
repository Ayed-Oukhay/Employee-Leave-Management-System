package com.projet.gestionconge.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.projet.gestionconge.IntegrationTest;
import com.projet.gestionconge.domain.TypeConge;
import com.projet.gestionconge.repository.TypeCongeRepository;
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
 * Integration tests for the {@link TypeCongeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeCongeResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-conges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeCongeRepository typeCongeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeCongeMockMvc;

    private TypeConge typeConge;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeConge createEntity(EntityManager em) {
        TypeConge typeConge = new TypeConge().nom(DEFAULT_NOM);
        return typeConge;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeConge createUpdatedEntity(EntityManager em) {
        TypeConge typeConge = new TypeConge().nom(UPDATED_NOM);
        return typeConge;
    }

    @BeforeEach
    public void initTest() {
        typeConge = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeConge() throws Exception {
        int databaseSizeBeforeCreate = typeCongeRepository.findAll().size();
        // Create the TypeConge
        restTypeCongeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeConge)))
            .andExpect(status().isCreated());

        // Validate the TypeConge in the database
        List<TypeConge> typeCongeList = typeCongeRepository.findAll();
        assertThat(typeCongeList).hasSize(databaseSizeBeforeCreate + 1);
        TypeConge testTypeConge = typeCongeList.get(typeCongeList.size() - 1);
        assertThat(testTypeConge.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createTypeCongeWithExistingId() throws Exception {
        // Create the TypeConge with an existing ID
        typeConge.setId(1L);

        int databaseSizeBeforeCreate = typeCongeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeCongeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeConge)))
            .andExpect(status().isBadRequest());

        // Validate the TypeConge in the database
        List<TypeConge> typeCongeList = typeCongeRepository.findAll();
        assertThat(typeCongeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTypeConges() throws Exception {
        // Initialize the database
        typeCongeRepository.saveAndFlush(typeConge);

        // Get all the typeCongeList
        restTypeCongeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeConge.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getTypeConge() throws Exception {
        // Initialize the database
        typeCongeRepository.saveAndFlush(typeConge);

        // Get the typeConge
        restTypeCongeMockMvc
            .perform(get(ENTITY_API_URL_ID, typeConge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeConge.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getNonExistingTypeConge() throws Exception {
        // Get the typeConge
        restTypeCongeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTypeConge() throws Exception {
        // Initialize the database
        typeCongeRepository.saveAndFlush(typeConge);

        int databaseSizeBeforeUpdate = typeCongeRepository.findAll().size();

        // Update the typeConge
        TypeConge updatedTypeConge = typeCongeRepository.findById(typeConge.getId()).get();
        // Disconnect from session so that the updates on updatedTypeConge are not directly saved in db
        em.detach(updatedTypeConge);
        updatedTypeConge.nom(UPDATED_NOM);

        restTypeCongeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTypeConge.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTypeConge))
            )
            .andExpect(status().isOk());

        // Validate the TypeConge in the database
        List<TypeConge> typeCongeList = typeCongeRepository.findAll();
        assertThat(typeCongeList).hasSize(databaseSizeBeforeUpdate);
        TypeConge testTypeConge = typeCongeList.get(typeCongeList.size() - 1);
        assertThat(testTypeConge.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingTypeConge() throws Exception {
        int databaseSizeBeforeUpdate = typeCongeRepository.findAll().size();
        typeConge.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeCongeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeConge.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeConge))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeConge in the database
        List<TypeConge> typeCongeList = typeCongeRepository.findAll();
        assertThat(typeCongeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeConge() throws Exception {
        int databaseSizeBeforeUpdate = typeCongeRepository.findAll().size();
        typeConge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeCongeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeConge))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeConge in the database
        List<TypeConge> typeCongeList = typeCongeRepository.findAll();
        assertThat(typeCongeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeConge() throws Exception {
        int databaseSizeBeforeUpdate = typeCongeRepository.findAll().size();
        typeConge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeCongeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeConge)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeConge in the database
        List<TypeConge> typeCongeList = typeCongeRepository.findAll();
        assertThat(typeCongeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeCongeWithPatch() throws Exception {
        // Initialize the database
        typeCongeRepository.saveAndFlush(typeConge);

        int databaseSizeBeforeUpdate = typeCongeRepository.findAll().size();

        // Update the typeConge using partial update
        TypeConge partialUpdatedTypeConge = new TypeConge();
        partialUpdatedTypeConge.setId(typeConge.getId());

        partialUpdatedTypeConge.nom(UPDATED_NOM);

        restTypeCongeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeConge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeConge))
            )
            .andExpect(status().isOk());

        // Validate the TypeConge in the database
        List<TypeConge> typeCongeList = typeCongeRepository.findAll();
        assertThat(typeCongeList).hasSize(databaseSizeBeforeUpdate);
        TypeConge testTypeConge = typeCongeList.get(typeCongeList.size() - 1);
        assertThat(testTypeConge.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void fullUpdateTypeCongeWithPatch() throws Exception {
        // Initialize the database
        typeCongeRepository.saveAndFlush(typeConge);

        int databaseSizeBeforeUpdate = typeCongeRepository.findAll().size();

        // Update the typeConge using partial update
        TypeConge partialUpdatedTypeConge = new TypeConge();
        partialUpdatedTypeConge.setId(typeConge.getId());

        partialUpdatedTypeConge.nom(UPDATED_NOM);

        restTypeCongeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeConge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeConge))
            )
            .andExpect(status().isOk());

        // Validate the TypeConge in the database
        List<TypeConge> typeCongeList = typeCongeRepository.findAll();
        assertThat(typeCongeList).hasSize(databaseSizeBeforeUpdate);
        TypeConge testTypeConge = typeCongeList.get(typeCongeList.size() - 1);
        assertThat(testTypeConge.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingTypeConge() throws Exception {
        int databaseSizeBeforeUpdate = typeCongeRepository.findAll().size();
        typeConge.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeCongeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeConge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeConge))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeConge in the database
        List<TypeConge> typeCongeList = typeCongeRepository.findAll();
        assertThat(typeCongeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeConge() throws Exception {
        int databaseSizeBeforeUpdate = typeCongeRepository.findAll().size();
        typeConge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeCongeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeConge))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeConge in the database
        List<TypeConge> typeCongeList = typeCongeRepository.findAll();
        assertThat(typeCongeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeConge() throws Exception {
        int databaseSizeBeforeUpdate = typeCongeRepository.findAll().size();
        typeConge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeCongeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(typeConge))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeConge in the database
        List<TypeConge> typeCongeList = typeCongeRepository.findAll();
        assertThat(typeCongeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeConge() throws Exception {
        // Initialize the database
        typeCongeRepository.saveAndFlush(typeConge);

        int databaseSizeBeforeDelete = typeCongeRepository.findAll().size();

        // Delete the typeConge
        restTypeCongeMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeConge.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeConge> typeCongeList = typeCongeRepository.findAll();
        assertThat(typeCongeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
