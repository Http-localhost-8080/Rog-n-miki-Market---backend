package bf.shopping.service.impl;

import bf.shopping.service.PannierService;
import bf.shopping.domain.Pannier;
import bf.shopping.repository.PannierRepository;
import bf.shopping.repository.search.PannierSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Pannier}.
 */
@Service
@Transactional
public class PannierServiceImpl implements PannierService {

    private final Logger log = LoggerFactory.getLogger(PannierServiceImpl.class);

    private final PannierRepository pannierRepository;

    private final PannierSearchRepository pannierSearchRepository;

    public PannierServiceImpl(PannierRepository pannierRepository, PannierSearchRepository pannierSearchRepository) {
        this.pannierRepository = pannierRepository;
        this.pannierSearchRepository = pannierSearchRepository;
    }

    /**
     * Save a pannier.
     *
     * @param pannier the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Pannier save(Pannier pannier) {
        log.debug("Request to save Pannier : {}", pannier);
        Pannier result = pannierRepository.save(pannier);
        pannierSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the panniers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Pannier> findAll(Pageable pageable) {
        log.debug("Request to get all Panniers");
        return pannierRepository.findAll(pageable);
    }

    /**
     * Get one pannier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Pannier> findOne(Long id) {
        log.debug("Request to get Pannier : {}", id);
        return pannierRepository.findById(id);
    }

    /**
     * Delete the pannier by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pannier : {}", id);
        pannierRepository.deleteById(id);
        pannierSearchRepository.deleteById(id);
    }

    /**
     * Search for the pannier corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Pannier> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Panniers for query {}", query);
        return pannierSearchRepository.search(queryStringQuery(query), pageable);    }
}
