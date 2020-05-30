package bf.shopping.repository.search;

import bf.shopping.domain.Picture;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Picture} entity.
 */
public interface PictureSearchRepository extends ElasticsearchRepository<Picture, Long> {
}
