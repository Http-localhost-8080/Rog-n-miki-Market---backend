package bf.shopping.service.impl;

import bf.shopping.domain.Etat;
import bf.shopping.domain.Picture;
import bf.shopping.repository.PictureRepository;
import bf.shopping.service.ArticleService;
import bf.shopping.domain.Article;
import bf.shopping.repository.ArticleRepository;
import bf.shopping.repository.search.ArticleSearchRepository;
import bf.shopping.service.EtatService;
import bf.shopping.service.PictureService;
import bf.shopping.service.dto.ArticleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Article}.
 */
@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final Logger log = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private final ArticleRepository articleRepository;
    private final PictureRepository pictureRepository;

    private final ArticleSearchRepository articleSearchRepository;
    private final PictureService pictureService;
    private final EtatService etatService;

    public ArticleServiceImpl(ArticleRepository articleRepository, PictureRepository pictureRepository, ArticleSearchRepository articleSearchRepository, PictureService pictureService, EtatService etatService) {
        this.articleRepository = articleRepository;
        this.pictureRepository = pictureRepository;
        this.articleSearchRepository = articleSearchRepository;
        this.pictureService = pictureService;
        this.etatService = etatService;
    }

    /**
     * Save a article.
     *  private String title;
     *
     *     private String description;
     *     private Double price;
     *     private String createAt;
     *     private Set<Picture> pictures = new HashSet<>();
     *     private City city;
     *     private Set<Note> notes = new HashSet<>();
     *     private Etat etat;
     *     private User user;
     *     private Set<Pannier> panniers = new HashSet<>();
     *     private Category category;
     *
     * @param articleDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Article save(ArticleDTO articleDTO) {
        log.debug("Request to save Article : {}", articleDTO);
        // Etat de l'article
        Etat etat = etatService.save(articleDTO.getEtat());

        // Nouvel article
        Article article = new Article();
        article.setId(articleDTO.getId());
        article.setTitle(articleDTO.getTitle());
        article.setDescription(articleDTO.getDescription());
        article.setPrice(articleDTO.getPrice());
        article.setCreateAt(new Date());
        article.setPictures(articleDTO.getPictures());
        article.setCity(articleDTO.getCity());
        article.setNotes(articleDTO.getNotes());
        article.setEtat(etat);
        article.setUser(articleDTO.getUser());
        article.setPanniers(articleDTO.getPanniers());
        article.setCategory(articleDTO.getCategory());
        Article result = articleRepository.save(article);

        // Enregistrement des images de l'article
        Set<Picture> pictures = articleDTO.getPictures();
        Iterator<Picture> iterator = pictures.iterator();
        Picture picture = null;
        while (iterator.hasNext()) {
            picture = iterator.next();
            picture.setArticle(result);
            pictureService.save(picture);
            picture = null;
        }

        articleSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the articles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Article> findAll(Pageable pageable) {
        log.debug("Request to get all Articles");
        return articleRepository.findAll(pageable);
    }

    /**
     * Get all the articles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Article> findAllWithEagerRelationships(Pageable pageable) {
        return articleRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one article by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Article> findOne(Long id) {
        log.debug("Request to get Article : {}", id);
        return articleRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the article by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Article : {}", id);
        articleRepository.deleteById(id);
        articleSearchRepository.deleteById(id);
    }

    /**
     * Search for the article corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Article> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Articles for query {}", query);
        return articleSearchRepository.search(queryStringQuery(query), pageable);    }

    @Override
    public List<Article> findArticlesWithPictures() {
        List<Article> articles = articleRepository.findAll();
        List<Article> mesArticles = new ArrayList<>();
        Article article = null;
        Set<Picture> pictures = new HashSet<>();

        int size = articles.size();
        if( size == 0) {
            return null;
        } else {
            for(int i = 0; i < size; i++) {
                article = articles.get(i);
                pictures = pictureRepository.findAllByArticle(article);
                article.setPictures(pictures);
                mesArticles.add(article);

                article = null;
                pictures = null;
            }

            return mesArticles;
        }
    }
}
