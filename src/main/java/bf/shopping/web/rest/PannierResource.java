package bf.shopping.web.rest;

import bf.shopping.domain.Pannier;
import bf.shopping.service.PannierService;
import bf.shopping.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link bf.shopping.domain.Pannier}.
 */
@RestController
@RequestMapping("/api")
public class PannierResource {

    private final Logger log = LoggerFactory.getLogger(PannierResource.class);

    private static final String ENTITY_NAME = "pannier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PannierService pannierService;

    public PannierResource(PannierService pannierService) {
        this.pannierService = pannierService;
    }

    /**
     * {@code POST  /panniers} : Create a new pannier.
     *
     * @param pannier the pannier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pannier, or with status {@code 400 (Bad Request)} if the pannier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/panniers")
    public ResponseEntity<Pannier> createPannier(@RequestBody Pannier pannier) throws URISyntaxException {
        log.debug("REST request to save Pannier : {}", pannier);
        if (pannier.getId() != null) {
            throw new BadRequestAlertException("A new pannier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pannier result = pannierService.save(pannier);
        return ResponseEntity.created(new URI("/api/panniers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /panniers} : Updates an existing pannier.
     *
     * @param pannier the pannier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pannier,
     * or with status {@code 400 (Bad Request)} if the pannier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pannier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/panniers")
    public ResponseEntity<Pannier> updatePannier(@RequestBody Pannier pannier) throws URISyntaxException {
        log.debug("REST request to update Pannier : {}", pannier);
        if (pannier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Pannier result = pannierService.save(pannier);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pannier.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /panniers} : get all the panniers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of panniers in body.
     */
    @GetMapping("/panniers")
    public ResponseEntity<List<Pannier>> getAllPanniers(Pageable pageable) {
        log.debug("REST request to get a page of Panniers");
        Page<Pannier> page = pannierService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /panniers/:id} : get the "id" pannier.
     *
     * @param id the id of the pannier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pannier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/panniers/{id}")
    public ResponseEntity<Pannier> getPannier(@PathVariable Long id) {
        log.debug("REST request to get Pannier : {}", id);
        Optional<Pannier> pannier = pannierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pannier);
    }

    /**
     * {@code DELETE  /panniers/:id} : delete the "id" pannier.
     *
     * @param id the id of the pannier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/panniers/{id}")
    public ResponseEntity<Void> deletePannier(@PathVariable Long id) {
        log.debug("REST request to delete Pannier : {}", id);
        pannierService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/panniers?query=:query} : search for the pannier corresponding
     * to the query.
     *
     * @param query the query of the pannier search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/panniers")
    public ResponseEntity<List<Pannier>> searchPanniers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Panniers for query {}", query);
        Page<Pannier> page = pannierService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
