package bf.shopping.repository.search;

import bf.shopping.domain.Note;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Note} entity.
 */
public interface NoteSearchRepository extends ElasticsearchRepository<Note, Long> {
}
