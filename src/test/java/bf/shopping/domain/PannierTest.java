package bf.shopping.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import bf.shopping.web.rest.TestUtil;

public class PannierTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pannier.class);
        Pannier pannier1 = new Pannier();
        pannier1.setId(1L);
        Pannier pannier2 = new Pannier();
        pannier2.setId(pannier1.getId());
        assertThat(pannier1).isEqualTo(pannier2);
        pannier2.setId(2L);
        assertThat(pannier1).isNotEqualTo(pannier2);
        pannier1.setId(null);
        assertThat(pannier1).isNotEqualTo(pannier2);
    }
}
