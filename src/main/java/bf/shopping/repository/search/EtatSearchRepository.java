package bf.shopping.repository.search;

import bf.shopping.domain.Etat;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Etat} entity.
 */
public interface EtatSearchRepository extends ElasticsearchRepository<Etat, Long> {
}
