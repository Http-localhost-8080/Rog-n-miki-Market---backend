package bf.shopping.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link EtatSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class EtatSearchRepositoryMockConfiguration {

    @MockBean
    private EtatSearchRepository mockEtatSearchRepository;

}
