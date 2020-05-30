package bf.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Pannier.
 */
@Entity
@Table(name = "pannier")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "pannier")
public class Pannier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantite")
    private Integer quantite;

    @Column(name = "price_total")
    private Double priceTotal;

    @Column(name = "is_free")
    private Boolean isFree;

    @ManyToMany(mappedBy = "panniers")
    @JsonIgnore
    private Set<Article> articles = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("panniers")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public Pannier quantite(Integer quantite) {
        this.quantite = quantite;
        return this;
    }

    public Pannier() {
    }

    public Boolean getFree() {
        return isFree;
    }

    public void setFree(Boolean free) {
        isFree = free;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Double getPriceTotal() {
        return priceTotal;
    }

    public Pannier priceTotal(Double priceTotal) {
        this.priceTotal = priceTotal;
        return this;
    }

    public void setPriceTotal(Double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public Pannier articles(Set<Article> articles) {
        this.articles = articles;
        return this;
    }

    public Pannier addArticle(Article article) {
        this.articles.add(article);
        article.getPanniers().add(this);
        return this;
    }

    public Pannier removeArticle(Article article) {
        this.articles.remove(article);
        article.getPanniers().remove(this);
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
        if (!(o instanceof Pannier)) {
            return false;
        }
        return id != null && id.equals(((Pannier) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Pannier{" +
            "id=" + getId() +
            ", quantite=" + getQuantite() +
            ", priceTotal=" + getPriceTotal() +
            "}";
    }
}
