package bf.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Article.
 */
@Entity
@Table(name = "article")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "create_at")
    private LocalDate createAt;

    @OneToMany(mappedBy = "article")
    private Set<Note> notes = new HashSet<>();

    @OneToMany(mappedBy = "article")
    private Set<Picture> pictures = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "article_city",
               joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "city_id", referencedColumnName = "id"))
    private Set<City> cities = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "article_etat",
               joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "etat_id", referencedColumnName = "id"))
    private Set<Etat> etats = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "article_user",
               joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> users = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "article_pannier",
               joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "pannier_id", referencedColumnName = "id"))
    private Set<Pannier> panniers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("articles")
    private Category category;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Article title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Article description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public Article price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public Article createAt(LocalDate createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public Article notes(Set<Note> notes) {
        this.notes = notes;
        return this;
    }

    public Article addNote(Note note) {
        this.notes.add(note);
        note.setArticle(this);
        return this;
    }

    public Article removeNote(Note note) {
        this.notes.remove(note);
        note.setArticle(null);
        return this;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    public Set<Picture> getPictures() {
        return pictures;
    }

    public Article pictures(Set<Picture> pictures) {
        this.pictures = pictures;
        return this;
    }

    public Article addPicture(Picture picture) {
        this.pictures.add(picture);
        picture.setArticle(this);
        return this;
    }

    public Article removePicture(Picture picture) {
        this.pictures.remove(picture);
        picture.setArticle(null);
        return this;
    }

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
    }

    public Set<City> getCities() {
        return cities;
    }

    public Article cities(Set<City> cities) {
        this.cities = cities;
        return this;
    }

    public Article addCity(City city) {
        this.cities.add(city);
        city.getArticles().add(this);
        return this;
    }

    public Article removeCity(City city) {
        this.cities.remove(city);
        city.getArticles().remove(this);
        return this;
    }

    public void setCities(Set<City> cities) {
        this.cities = cities;
    }

    public Set<Etat> getEtats() {
        return etats;
    }

    public Article etats(Set<Etat> etats) {
        this.etats = etats;
        return this;
    }

    public Article addEtat(Etat etat) {
        this.etats.add(etat);
        etat.getArticles().add(this);
        return this;
    }

    public Article removeEtat(Etat etat) {
        this.etats.remove(etat);
        etat.getArticles().remove(this);
        return this;
    }

    public void setEtats(Set<Etat> etats) {
        this.etats = etats;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Article users(Set<User> users) {
        this.users = users;
        return this;
    }

    public Article addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Article removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Pannier> getPanniers() {
        return panniers;
    }

    public Article panniers(Set<Pannier> panniers) {
        this.panniers = panniers;
        return this;
    }

    public Article addPannier(Pannier pannier) {
        this.panniers.add(pannier);
        pannier.getArticles().add(this);
        return this;
    }

    public Article removePannier(Pannier pannier) {
        this.panniers.remove(pannier);
        pannier.getArticles().remove(this);
        return this;
    }

    public void setPanniers(Set<Pannier> panniers) {
        this.panniers = panniers;
    }

    public Category getCategory() {
        return category;
    }

    public Article category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Article)) {
            return false;
        }
        return id != null && id.equals(((Article) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Article{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", createAt='" + getCreateAt() + "'" +
            "}";
    }
}
