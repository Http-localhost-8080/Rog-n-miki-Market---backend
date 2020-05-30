package bf.shopping.repository.search;

import bf.shopping.domain.Pannier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Pannier} entity.
 */
public interface PannierSearchRepository extends ElasticsearchRepository<Pannier, Long> {
}
