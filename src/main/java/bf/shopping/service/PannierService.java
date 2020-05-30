package bf.shopping.service;

import bf.shopping.domain.Pannier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Pannier}.
 */
public interface PannierService {

    /**
     * Save a pannier.
     *
     * @param pannier the entity to save.
     * @return the persisted entity.
     */
    Pannier save(Pannier pannier);

    /**
     * Get all the panniers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Pannier> findAll(Pageable pageable);

    /**
     * Get the "id" pannier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Pannier> findOne(Long id);

    /**
     * Delete the "id" pannier.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the pannier corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Pannier> search(String query, Pageable pageable);
}
