package bf.shopping.web.rest;

import bf.shopping.ShoppingApp;
import bf.shopping.domain.Picture;
import bf.shopping.repository.PictureRepository;
import bf.shopping.repository.search.PictureSearchRepository;
import bf.shopping.service.PictureService;
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
import org.springframework.util.Base64Utils;
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
 * Integration tests for the {@link PictureResource} REST controller.
 */
@SpringBootTest(classes = ShoppingApp.class)
public class PictureResourceIT {

    private static final byte[] DEFAULT_NAME = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_NAME = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_NAME_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_NAME_CONTENT_TYPE = "image/png";

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private PictureService pictureService;

    /**
     * This repository is mocked in the bf.shopping.repository.search test package.
     *
     * @see bf.shopping.repository.search.PictureSearchRepositoryMockConfiguration
     */
    @Autowired
    private PictureSearchRepository mockPictureSearchRepository;

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

    private MockMvc restPictureMockMvc;

    private Picture picture;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PictureResource pictureResource = new PictureResource(pictureService);
        this.restPictureMockMvc = MockMvcBuilders.standaloneSetup(pictureResource)
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
    public static Picture createEntity(EntityManager em) {
        Picture picture = new Picture()
            .name(DEFAULT_NAME)
            .nameContentType(DEFAULT_NAME_CONTENT_TYPE);
        return picture;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Picture createUpdatedEntity(EntityManager em) {
        Picture picture = new Picture()
            .name(UPDATED_NAME)
            .nameContentType(UPDATED_NAME_CONTENT_TYPE);
        return picture;
    }

    @BeforeEach
    public void initTest() {
        picture = createEntity(em);
    }

    @Test
    @Transactional
    public void createPicture() throws Exception {
        int databaseSizeBeforeCreate = pictureRepository.findAll().size();

        // Create the Picture
        restPictureMockMvc.perform(post("/api/pictures")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(picture)))
            .andExpect(status().isCreated());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeCreate + 1);
        Picture testPicture = pictureList.get(pictureList.size() - 1);
        assertThat(testPicture.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPicture.getNameContentType()).isEqualTo(DEFAULT_NAME_CONTENT_TYPE);

        // Validate the Picture in Elasticsearch
        verify(mockPictureSearchRepository, times(1)).save(testPicture);
    }

    @Test
    @Transactional
    public void createPictureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pictureRepository.findAll().size();

        // Create the Picture with an existing ID
        picture.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPictureMockMvc.perform(post("/api/pictures")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(picture)))
            .andExpect(status().isBadRequest());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeCreate);

        // Validate the Picture in Elasticsearch
        verify(mockPictureSearchRepository, times(0)).save(picture);
    }


    @Test
    @Transactional
    public void getAllPictures() throws Exception {
        // Initialize the database
        pictureRepository.saveAndFlush(picture);

        // Get all the pictureList
        restPictureMockMvc.perform(get("/api/pictures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(picture.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameContentType").value(hasItem(DEFAULT_NAME_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(Base64Utils.encodeToString(DEFAULT_NAME))));
    }
    
    @Test
    @Transactional
    public void getPicture() throws Exception {
        // Initialize the database
        pictureRepository.saveAndFlush(picture);

        // Get the picture
        restPictureMockMvc.perform(get("/api/pictures/{id}", picture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(picture.getId().intValue()))
            .andExpect(jsonPath("$.nameContentType").value(DEFAULT_NAME_CONTENT_TYPE))
            .andExpect(jsonPath("$.name").value(Base64Utils.encodeToString(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void getNonExistingPicture() throws Exception {
        // Get the picture
        restPictureMockMvc.perform(get("/api/pictures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePicture() throws Exception {
        // Initialize the database
        pictureService.save(picture);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPictureSearchRepository);

        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();

        // Update the picture
        Picture updatedPicture = pictureRepository.findById(picture.getId()).get();
        // Disconnect from session so that the updates on updatedPicture are not directly saved in db
        em.detach(updatedPicture);
        updatedPicture
            .name(UPDATED_NAME)
            .nameContentType(UPDATED_NAME_CONTENT_TYPE);

        restPictureMockMvc.perform(put("/api/pictures")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPicture)))
            .andExpect(status().isOk());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeUpdate);
        Picture testPicture = pictureList.get(pictureList.size() - 1);
        assertThat(testPicture.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPicture.getNameContentType()).isEqualTo(UPDATED_NAME_CONTENT_TYPE);

        // Validate the Picture in Elasticsearch
        verify(mockPictureSearchRepository, times(1)).save(testPicture);
    }

    @Test
    @Transactional
    public void updateNonExistingPicture() throws Exception {
        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();

        // Create the Picture

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPictureMockMvc.perform(put("/api/pictures")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(picture)))
            .andExpect(status().isBadRequest());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Picture in Elasticsearch
        verify(mockPictureSearchRepository, times(0)).save(picture);
    }

    @Test
    @Transactional
    public void deletePicture() throws Exception {
        // Initialize the database
        pictureService.save(picture);

        int databaseSizeBeforeDelete = pictureRepository.findAll().size();

        // Delete the picture
        restPictureMockMvc.perform(delete("/api/pictures/{id}", picture.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Picture in Elasticsearch
        verify(mockPictureSearchRepository, times(1)).deleteById(picture.getId());
    }

    @Test
    @Transactional
    public void searchPicture() throws Exception {
        // Initialize the database
        pictureService.save(picture);
        when(mockPictureSearchRepository.search(queryStringQuery("id:" + picture.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(picture), PageRequest.of(0, 1), 1));
        // Search the picture
        restPictureMockMvc.perform(get("/api/_search/pictures?query=id:" + picture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(picture.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameContentType").value(hasItem(DEFAULT_NAME_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(Base64Utils.encodeToString(DEFAULT_NAME))));
    }
}
