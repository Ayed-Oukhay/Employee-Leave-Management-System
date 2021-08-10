package com.projet.gestionconge.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.projet.gestionconge.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeCongeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeConge.class);
        TypeConge typeConge1 = new TypeConge();
        typeConge1.setId(1L);
        TypeConge typeConge2 = new TypeConge();
        typeConge2.setId(typeConge1.getId());
        assertThat(typeConge1).isEqualTo(typeConge2);
        typeConge2.setId(2L);
        assertThat(typeConge1).isNotEqualTo(typeConge2);
        typeConge1.setId(null);
        assertThat(typeConge1).isNotEqualTo(typeConge2);
    }
}
