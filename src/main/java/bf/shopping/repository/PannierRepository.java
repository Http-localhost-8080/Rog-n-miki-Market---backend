package bf.shopping.repository;

import bf.shopping.domain.Pannier;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Pannier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PannierRepository extends JpaRepository<Pannier, Long> {

}
