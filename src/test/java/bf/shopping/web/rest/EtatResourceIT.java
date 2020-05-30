package bf.shopping.web.rest;

import bf.shopping.ShoppingApp;
import bf.shopping.domain.Etat;
import bf.shopping.repository.EtatRepository;
import bf.shopping.repository.search.EtatSearchRepository;
import bf.shopping.service.EtatService;
import bf.shopping.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
 * Integration tests for the {@link EtatResource} REST controller.
 */
@SpringBootTest(classes = ShoppingApp.class)
public class EtatResourceIT {

    private static final Boolean DEFAULT_AVAILABLE = false;
    private static final Boolean UPDATED_AVAILABLE = true;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Double DEFAULT_FRAIS = 1D;
    private static final Double UPDATED_FRAIS = 2D;

    @Autowired
    private EtatRepository etatRepository;

    @Autowired
    private EtatService etatService;

    /**
     * This repository is mocked in the bf.shopping.repository.search test package.
     *
     * @see bf.shopping.repository.search.EtatSearchRepositoryMockConfiguration
     */
    @Autowired
    private EtatSearchRepository mockEtatSearchRepository;

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

    private MockMvc restEtatMockMvc;

    private Etat etat;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EtatResource etatResource = new EtatResource(etatService);
        this.restEtatMockMvc = MockMvcBuilders.standaloneSetup(etatResource)
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
    public static Etat createEntity(EntityManager em) {
        Etat etat = new Etat()
            .available(DEFAULT_AVAILABLE)
            .type(DEFAULT_TYPE)
            .frais(DEFAULT_FRAIS);
        return etat;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etat createUpdatedEntity(EntityManager em) {
        Etat etat = new Etat()
            .available(UPDATED_AVAILABLE)
            .type(UPDATED_TYPE)
            .frais(UPDATED_FRAIS);
        return etat;
    }

    @BeforeEach
    public void initTest() {
        etat = createEntity(em);
    }

    @Test
    @Transactional
    public void createEtat() throws Exception {
        int databaseSizeBeforeCreate = etatRepository.findAll().size();

        // Create the Etat
        restEtatMockMvc.perform(post("/api/etats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(etat)))
            .andExpect(status().isCreated());

        // Validate the Etat in the database
        List<Etat> etatList = etatRepository.findAll();
        assertThat(etatList).hasSize(databaseSizeBeforeCreate + 1);
        Etat testEtat = etatList.get(etatList.size() - 1);
        assertThat(testEtat.isAvailable()).isEqualTo(DEFAULT_AVAILABLE);
        assertThat(testEtat.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testEtat.getFrais()).isEqualTo(DEFAULT_FRAIS);

        // Validate the Etat in Elasticsearch
        verify(mockEtatSearchRepository, times(1)).save(testEtat);
    }

    @Test
    @Transactional
    public void createEtatWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = etatRepository.findAll().size();

        // Create the Etat with an existing ID
        etat.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtatMockMvc.perform(post("/api/etats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(etat)))
            .andExpect(status().isBadRequest());

        // Validate the Etat in the database
        List<Etat> etatList = etatRepository.findAll();
        assertThat(etatList).hasSize(databaseSizeBeforeCreate);

        // Validate the Etat in Elasticsearch
        verify(mockEtatSearchRepository, times(0)).save(etat);
    }


    @Test
    @Transactional
    public void checkAvailableIsRequired() throws Exception {
        int databaseSizeBeforeTest = etatRepository.findAll().size();
        // set the field null
        etat.setAvailable(null);

        // Create the Etat, which fails.

        restEtatMockMvc.perform(post("/api/etats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(etat)))
            .andExpect(status().isBadRequest());

        List<Etat> etatList = etatRepository.findAll();
        assertThat(etatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = etatRepository.findAll().size();
        // set the field null
        etat.setType(null);

        // Create the Etat, which fails.

        restEtatMockMvc.perform(post("/api/etats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(etat)))
            .andExpect(status().isBadRequest());

        List<Etat> etatList = etatRepository.findAll();
        assertThat(etatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEtats() throws Exception {
        // Initialize the database
        etatRepository.saveAndFlush(etat);

        // Get all the etatList
        restEtatMockMvc.perform(get("/api/etats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etat.getId().intValue())))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].frais").value(hasItem(DEFAULT_FRAIS.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getEtat() throws Exception {
        // Initialize the database
        etatRepository.saveAndFlush(etat);

        // Get the etat
        restEtatMockMvc.perform(get("/api/etats/{id}", etat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(etat.getId().intValue()))
            .andExpect(jsonPath("$.available").value(DEFAULT_AVAILABLE.booleanValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.frais").value(DEFAULT_FRAIS.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEtat() throws Exception {
        // Get the etat
        restEtatMockMvc.perform(get("/api/etats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEtat() throws Exception {
        // Initialize the database
        etatService.save(etat);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockEtatSearchRepository);

        int databaseSizeBeforeUpdate = etatRepository.findAll().size();

        // Update the etat
        Etat updatedEtat = etatRepository.findById(etat.getId()).get();
        // Disconnect from session so that the updates on updatedEtat are not directly saved in db
        em.detach(updatedEtat);
        updatedEtat
            .available(UPDATED_AVAILABLE)
            .type(UPDATED_TYPE)
            .frais(UPDATED_FRAIS);

        restEtatMockMvc.perform(put("/api/etats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedEtat)))
            .andExpect(status().isOk());

        // Validate the Etat in the database
        List<Etat> etatList = etatRepository.findAll();
        assertThat(etatList).hasSize(databaseSizeBeforeUpdate);
        Etat testEtat = etatList.get(etatList.size() - 1);
        assertThat(testEtat.isAvailable()).isEqualTo(UPDATED_AVAILABLE);
        assertThat(testEtat.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testEtat.getFrais()).isEqualTo(UPDATED_FRAIS);

        // Validate the Etat in Elasticsearch
        verify(mockEtatSearchRepository, times(1)).save(testEtat);
    }

    @Test
    @Transactional
    public void updateNonExistingEtat() throws Exception {
        int databaseSizeBeforeUpdate = etatRepository.findAll().size();

        // Create the Etat

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtatMockMvc.perform(put("/api/etats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(etat)))
            .andExpect(status().isBadRequest());

        // Validate the Etat in the database
        List<Etat> etatList = etatRepository.findAll();
        assertThat(etatList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Etat in Elasticsearch
        verify(mockEtatSearchRepository, times(0)).save(etat);
    }

    @Test
    @Transactional
    public void deleteEtat() throws Exception {
        // Initialize the database
        etatService.save(etat);

        int databaseSizeBeforeDelete = etatRepository.findAll().size();

        // Delete the etat
        restEtatMockMvc.perform(delete("/api/etats/{id}", etat.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Etat> etatList = etatRepository.findAll();
        assertThat(etatList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Etat in Elasticsearch
        verify(mockEtatSearchRepository, times(1)).deleteById(etat.getId());
    }

    @Test
    @Transactional
    public void searchEtat() throws Exception {
        // Initialize the database
        etatService.save(etat);
        when(mockEtatSearchRepository.search(queryStringQuery("id:" + etat.getId())))
            .thenReturn(Collections.singletonList(etat));
        // Search the etat
        restEtatMockMvc.perform(get("/api/_search/etats?query=id:" + etat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etat.getId().intValue())))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].frais").value(hasItem(DEFAULT_FRAIS.doubleValue())));
    }
}
