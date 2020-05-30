package bf.shopping.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PannierSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PannierSearchRepositoryMockConfiguration {

    @MockBean
    private PannierSearchRepository mockPannierSearchRepository;

}
