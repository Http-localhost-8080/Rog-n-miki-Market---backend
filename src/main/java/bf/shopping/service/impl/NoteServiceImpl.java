package bf.shopping.service.impl;

import bf.shopping.service.NoteService;
import bf.shopping.domain.Note;
import bf.shopping.repository.NoteRepository;
import bf.shopping.repository.search.NoteSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Note}.
 */
@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    private final Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);

    private final NoteRepository noteRepository;

    private final NoteSearchRepository noteSearchRepository;

    public NoteServiceImpl(NoteRepository noteRepository, NoteSearchRepository noteSearchRepository) {
        this.noteRepository = noteRepository;
        this.noteSearchRepository = noteSearchRepository;
    }

    /**
     * Save a note.
     *
     * @param note the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Note save(Note note) {
        log.debug("Request to save Note : {}", note);
        Note result = noteRepository.save(note);
        noteSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the notes.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Note> findAll() {
        log.debug("Request to get all Notes");
        return noteRepository.findAll();
    }

    /**
     * Get one note by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Note> findOne(Long id) {
        log.debug("Request to get Note : {}", id);
        return noteRepository.findById(id);
    }

    /**
     * Delete the note by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Note : {}", id);
        noteRepository.deleteById(id);
        noteSearchRepository.deleteById(id);
    }

    /**
     * Search for the note corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Note> search(String query) {
        log.debug("Request to search Notes for query {}", query);
        return StreamSupport
            .stream(noteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
