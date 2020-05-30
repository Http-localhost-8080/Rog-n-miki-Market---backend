package bf.shopping.service.impl;

import bf.shopping.service.EtatService;
import bf.shopping.domain.Etat;
import bf.shopping.repository.EtatRepository;
import bf.shopping.repository.search.EtatSearchRepository;
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
 * Service Implementation for managing {@link Etat}.
 */
@Service
@Transactional
public class EtatServiceImpl implements EtatService {

    private final Logger log = LoggerFactory.getLogger(EtatServiceImpl.class);

    private final EtatRepository etatRepository;

    private final EtatSearchRepository etatSearchRepository;

    public EtatServiceImpl(EtatRepository etatRepository, EtatSearchRepository etatSearchRepository) {
        this.etatRepository = etatRepository;
        this.etatSearchRepository = etatSearchRepository;
    }

    /**
     * Save a etat.
     *
     * @param etat the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Etat save(Etat etat) {
        log.debug("Request to save Etat : {}", etat);
        Etat result = etatRepository.save(etat);
        etatSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the etats.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Etat> findAll() {
        log.debug("Request to get all Etats");
        return etatRepository.findAll();
    }

    /**
     * Get one etat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Etat> findOne(Long id) {
        log.debug("Request to get Etat : {}", id);
        return etatRepository.findById(id);
    }

    /**
     * Delete the etat by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Etat : {}", id);
        etatRepository.deleteById(id);
        etatSearchRepository.deleteById(id);
    }

    /**
     * Search for the etat corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Etat> search(String query) {
        log.debug("Request to search Etats for query {}", query);
        return StreamSupport
            .stream(etatSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
