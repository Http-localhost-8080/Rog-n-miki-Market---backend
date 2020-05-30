package bf.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Picture.
 */
@Entity
@Table(name = "picture")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "picture")
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Lob
    @Column(name = "name", nullable = false)
    private byte[] name;

    @Column(name = "name_content_type", nullable = false)
    private String nameContentType;

    @ManyToOne
    @JsonIgnoreProperties("pictures")
    private Article article;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getName() {
        return name;
    }

    public Picture name(byte[] name) {
        this.name = name;
        return this;
    }

    public void setName(byte[] name) {
        this.name = name;
    }

    public String getNameContentType() {
        return nameContentType;
    }

    public Picture nameContentType(String nameContentType) {
        this.nameContentType = nameContentType;
        return this;
    }

    public void setNameContentType(String nameContentType) {
        this.nameContentType = nameContentType;
    }

    public Article getArticle() {
        return article;
    }

    public Picture article(Article article) {
        this.article = article;
        return this;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Picture)) {
            return false;
        }
        return id != null && id.equals(((Picture) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Picture{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", nameContentType='" + getNameContentType() + "'" +
            "}";
    }
}
