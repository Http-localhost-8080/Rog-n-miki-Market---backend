package bf.shopping.service;

import bf.shopping.domain.Etat;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Etat}.
 */
public interface EtatService {

    /**
     * Save a etat.
     *
     * @param etat the entity to save.
     * @return the persisted entity.
     */
    Etat save(Etat etat);

    /**
     * Get all the etats.
     *
     * @return the list of entities.
     */
    List<Etat> findAll();

    /**
     * Get the "id" etat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Etat> findOne(Long id);

    /**
     * Delete the "id" etat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the etat corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<Etat> search(String query);
}
