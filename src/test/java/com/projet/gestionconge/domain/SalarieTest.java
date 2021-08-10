package com.projet.gestionconge.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.projet.gestionconge.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalarieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Salarie.class);
        Salarie salarie1 = new Salarie();
        salarie1.setId(1L);
        Salarie salarie2 = new Salarie();
        salarie2.setId(salarie1.getId());
        assertThat(salarie1).isEqualTo(salarie2);
        salarie2.setId(2L);
        assertThat(salarie1).isNotEqualTo(salarie2);
        salarie1.setId(null);
        assertThat(salarie1).isNotEqualTo(salarie2);
    }
}
