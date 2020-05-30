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
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "frais")
    private Double frais;

    @ManyToMany(mappedBy = "etats")
    @JsonIgnore
    private Set<Article> articles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isAvailable() {
        return available;
    }

    public Etat available(Boolean available) {
        this.available = available;
        return this;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getType() {
        return type;
    }

    public Etat type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getFrais() {
        return frais;
    }

    public Etat frais(Double frais) {
        this.frais = frais;
        return this;
    }

    public void setFrais(Double frais) {
        this.frais = frais;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public Etat articles(Set<Article> articles) {
        this.articles = articles;
        return this;
    }

    public Etat addArticle(Article article) {
        this.articles.add(article);
        article.getEtats().add(this);
        return this;
    }

    public Etat removeArticle(Article article) {
        this.articles.remove(article);
        article.getEtats().remove(this);
        return this;
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
            ", available='" + isAvailable() + "'" +
            ", type='" + getType() + "'" +
            ", frais=" + getFrais() +
            "}";
    }
}
