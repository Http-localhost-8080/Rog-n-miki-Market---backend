package bf.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Etat.
 */
@Entity
@Table(name = "etat")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "etat")
public class Etat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "available", nullable = false)
    private Boolean available;

    @NotNull
    @Column(name = "mode_acquisition", nullable = false)
    private String modeAcquisition;

    @NotNull
    @Column(name = "etat_article", nullable = false)
    private String etatArticle;

    @Column(name = "frais_livraison")
    private Double fraisLivraison;

    @OneToMany(mappedBy = "etat")
    private Set<Article> articles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Etat() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getModeAcquisition() {
        return modeAcquisition;
    }

    public void setModeAcquisition(String modeAcquisition) {
        this.modeAcquisition = modeAcquisition;
    }

    public String getEtatArticle() {
        return etatArticle;
    }

    public void setEtatArticle(String etatArticle) {
        this.etatArticle = etatArticle;
    }

    public Double getFraisLivraison() {
        return fraisLivraison;
    }

    public void setFraisLivraison(Double fraisLivraison) {
        this.fraisLivraison = fraisLivraison;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }
// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etat)) {
            return false;
        }
        return id != null && id.equals(((Etat) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Etat{" +
            "id=" + getId() +
            ", available='" + getAvailable() + "'" +
            ", etatArticle ='" + getEtatArticle() +
            ", mode acquistion='" + getModeAcquisition() + "'" +
            ", frais=" + getFraisLivraison() +
            "}";
    }
}
