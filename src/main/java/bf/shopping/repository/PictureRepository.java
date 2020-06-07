package bf.shopping.repository;

import bf.shopping.domain.Article;
import bf.shopping.domain.Picture;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Spring Data  repository for the Picture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {
    Set<Picture> findAllByArticle(Article article);
}
