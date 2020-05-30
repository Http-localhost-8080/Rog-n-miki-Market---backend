package bf.shopping.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import bf.shopping.web.rest.TestUtil;

public class EtatTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Etat.class);
        Etat etat1 = new Etat();
        etat1.setId(1L);
        Etat etat2 = new Etat();
        etat2.setId(etat1.getId());
        assertThat(etat1).isEqualTo(etat2);
        etat2.setId(2L);
        assertThat(etat1).isNotEqualTo(etat2);
        etat1.setId(null);
        assertThat(etat1).isNotEqualTo(etat2);
    }
}
