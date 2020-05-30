package bf.shopping.web.rest;

import bf.shopping.domain.Etat;
import bf.shopping.service.EtatService;
import bf.shopping.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link bf.shopping.domain.Etat}.
 */
@RestController
@RequestMapping("/api")
public class EtatResource {

    private final Logger log = LoggerFactory.getLogger(EtatResource.class);

    private static final String ENTITY_NAME = "etat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EtatService etatService;

    public EtatResource(EtatService etatService) {
        this.etatService = etatService;
    }

    /**
     * {@code POST  /etats} : Create a new etat.
     *
     * @param etat the etat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new etat, or with status {@code 400 (Bad Request)} if the etat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/etats")
    public ResponseEntity<Etat> createEtat(@Valid @RequestBody Etat etat) throws URISyntaxException {
        log.debug("REST request to save Etat : {}", etat);
        if (etat.getId() != null) {
            throw new BadRequestAlertException("A new etat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Etat result = etatService.save(etat);
        return ResponseEntity.created(new URI("/api/etats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /etats} : Updates an existing etat.
     *
     * @param etat the etat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etat,
     * or with status {@code 400 (Bad Request)} if the etat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the etat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/etats")
    public ResponseEntity<Etat> updateEtat(@Valid @RequestBody Etat etat) throws URISyntaxException {
        log.debug("REST request to update Etat : {}", etat);
        if (etat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Etat result = etatService.save(etat);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, etat.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /etats} : get all the etats.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of etats in body.
     */
    @GetMapping("/etats")
    public List<Etat> getAllEtats() {
        log.debug("REST request to get all Etats");
        return etatService.findAll();
    }

    /**
     * {@code GET  /etats/:id} : get the "id" etat.
     *
     * @param id the id of the etat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the etat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/etats/{id}")
    public ResponseEntity<Etat> getEtat(@PathVariable Long id) {
        log.debug("REST request to get Etat : {}", id);
        Optional<Etat> etat = etatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(etat);
    }

    /**
     * {@code DELETE  /etats/:id} : delete the "id" etat.
     *
     * @param id the id of the etat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/etats/{id}")
    public ResponseEntity<Void> deleteEtat(@PathVariable Long id) {
        log.debug("REST request to delete Etat : {}", id);
        etatService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/etats?query=:query} : search for the etat corresponding
     * to the query.
     *
     * @param query the query of the etat search.
     * @return the result of the search.
     */
    @GetMapping("/_search/etats")
    public List<Etat> searchEtats(@RequestParam String query) {
        log.debug("REST request to search Etats for query {}", query);
        return etatService.search(query);
    }
}
