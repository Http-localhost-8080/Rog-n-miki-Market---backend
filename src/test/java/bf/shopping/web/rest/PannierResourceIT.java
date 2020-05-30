package bf.shopping.web.rest;

import bf.shopping.ShoppingApp;
import bf.shopping.domain.Pannier;
import bf.shopping.repository.PannierRepository;
import bf.shopping.repository.search.PannierSearchRepository;
import bf.shopping.service.PannierService;
import bf.shopping.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static bf.shopping.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PannierResource} REST controller.
 */
@SpringBootTest(classes = ShoppingApp.class)
public class PannierResourceIT {

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final Double DEFAULT_PRICE_TOTAL = 1D;
    private static final Double UPDATED_PRICE_TOTAL = 2D;

    @Autowired
    private PannierRepository pannierRepository;

    @Autowired
    private PannierService pannierService;

    /**
     * This repository is mocked in the bf.shopping.repository.search test package.
     *
     * @see bf.shopping.repository.search.PannierSearchRepositoryMockConfiguration
     */
    @Autowired
    private PannierSearchRepository mockPannierSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPannierMockMvc;

    private Pannier pannier;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PannierResource pannierResource = new PannierResource(pannierService);
        this.restPannierMockMvc = MockMvcBuilders.standaloneSetup(pannierResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pannier createEntity(EntityManager em) {
        Pannier pannier = new Pannier()
            .quantite(DEFAULT_QUANTITE)
            .priceTotal(DEFAULT_PRICE_TOTAL);
        return pannier;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pannier createUpdatedEntity(EntityManager em) {
        Pannier pannier = new Pannier()
            .quantite(UPDATED_QUANTITE)
            .priceTotal(UPDATED_PRICE_TOTAL);
        return pannier;
    }

    @BeforeEach
    public void initTest() {
        pannier = createEntity(em);
    }

    @Test
    @Transactional
    public void createPannier() throws Exception {
        int databaseSizeBeforeCreate = pannierRepository.findAll().size();

        // Create the Pannier
        restPannierMockMvc.perform(post("/api/panniers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pannier)))
            .andExpect(status().isCreated());

        // Validate the Pannier in the database
        List<Pannier> pannierList = pannierRepository.findAll();
        assertThat(pannierList).hasSize(databaseSizeBeforeCreate + 1);
        Pannier testPannier = pannierList.get(pannierList.size() - 1);
        assertThat(testPannier.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testPannier.getPriceTotal()).isEqualTo(DEFAULT_PRICE_TOTAL);

        // Validate the Pannier in Elasticsearch
        verify(mockPannierSearchRepository, times(1)).save(testPannier);
    }

    @Test
    @Transactional
    public void createPannierWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pannierRepository.findAll().size();

        // Create the Pannier with an existing ID
        pannier.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPannierMockMvc.perform(post("/api/panniers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pannier)))
            .andExpect(status().isBadRequest());

        // Validate the Pannier in the database
        List<Pannier> pannierList = pannierRepository.findAll();
        assertThat(pannierList).hasSize(databaseSizeBeforeCreate);

        // Validate the Pannier in Elasticsearch
        verify(mockPannierSearchRepository, times(0)).save(pannier);
    }


    @Test
    @Transactional
    public void getAllPanniers() throws Exception {
        // Initialize the database
        pannierRepository.saveAndFlush(pannier);

        // Get all the pannierList
        restPannierMockMvc.perform(get("/api/panniers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pannier.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].priceTotal").value(hasItem(DEFAULT_PRICE_TOTAL.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getPannier() throws Exception {
        // Initialize the database
        pannierRepository.saveAndFlush(pannier);

        // Get the pannier
        restPannierMockMvc.perform(get("/api/panniers/{id}", pannier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pannier.getId().intValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.priceTotal").value(DEFAULT_PRICE_TOTAL.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPannier() throws Exception {
        // Get the pannier
        restPannierMockMvc.perform(get("/api/panniers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePannier() throws Exception {
        // Initialize the database
        pannierService.save(pannier);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPannierSearchRepository);

        int databaseSizeBeforeUpdate = pannierRepository.findAll().size();

        // Update the pannier
        Pannier updatedPannier = pannierRepository.findById(pannier.getId()).get();
        // Disconnect from session so that the updates on updatedPannier are not directly saved in db
        em.detach(updatedPannier);
        updatedPannier
            .quantite(UPDATED_QUANTITE)
            .priceTotal(UPDATED_PRICE_TOTAL);

        restPannierMockMvc.perform(put("/api/panniers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPannier)))
            .andExpect(status().isOk());

        // Validate the Pannier in the database
        List<Pannier> pannierList = pannierRepository.findAll();
        assertThat(pannierList).hasSize(databaseSizeBeforeUpdate);
        Pannier testPannier = pannierList.get(pannierList.size() - 1);
        assertThat(testPannier.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testPannier.getPriceTotal()).isEqualTo(UPDATED_PRICE_TOTAL);

        // Validate the Pannier in Elasticsearch
        verify(mockPannierSearchRepository, times(1)).save(testPannier);
    }

    @Test
    @Transactional
    public void updateNonExistingPannier() throws Exception {
        int databaseSizeBeforeUpdate = pannierRepository.findAll().size();

        // Create the Pannier

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPannierMockMvc.perform(put("/api/panniers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pannier)))
            .andExpect(status().isBadRequest());

        // Validate the Pannier in the database
        List<Pannier> pannierList = pannierRepository.findAll();
        assertThat(pannierList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Pannier in Elasticsearch
        verify(mockPannierSearchRepository, times(0)).save(pannier);
    }

    @Test
    @Transactional
    public void deletePannier() throws Exception {
        // Initialize the database
        pannierService.save(pannier);

        int databaseSizeBeforeDelete = pannierRepository.findAll().size();

        // Delete the pannier
        restPannierMockMvc.perform(delete("/api/panniers/{id}", pannier.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pannier> pannierList = pannierRepository.findAll();
        assertThat(pannierList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Pannier in Elasticsearch
        verify(mockPannierSearchRepository, times(1)).deleteById(pannier.getId());
    }

    @Test
    @Transactional
    public void searchPannier() throws Exception {
        // Initialize the database
        pannierService.save(pannier);
        when(mockPannierSearchRepository.search(queryStringQuery("id:" + pannier.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(pannier), PageRequest.of(0, 1), 1));
        // Search the pannier
        restPannierMockMvc.perform(get("/api/_search/panniers?query=id:" + pannier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pannier.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].priceTotal").value(hasItem(DEFAULT_PRICE_TOTAL.doubleValue())));
    }
}
