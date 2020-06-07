package bf.shopping.service.dto;

import bf.shopping.domain.*;

import java.util.HashSet;
import java.util.Set;

public class ArticleDTO {

    private Long id;


    private String title;


    private String description;


    private Double price;


    private String createAt;

    private Set<Picture> pictures = new HashSet<>();

    private City city;

    private Set<Note> notes = new HashSet<>();

    private Etat etat;

    private User user;

    private Set<Pannier> panniers = new HashSet<>();

    private Category category;

    public ArticleDTO() {
    }

    public ArticleDTO(Long id, String title, String description, Double price, String createAt, Set<Picture> pictures, City city, Set<Note> notes, Etat etat, User user, Set<Pannier> panniers, Category category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.createAt = createAt;
        this.pictures = pictures;
        this.city = city;
        this.notes = notes;
        this.etat = etat;
        this.user = user;
        this.panniers = panniers;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public Set<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Pannier> getPanniers() {
        return panniers;
    }

    public void setPanniers(Set<Pannier> panniers) {
        this.panniers = panniers;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "ArticleDTO { " +
            "id=" + id +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", price=" + price +
            ", createAt='" + createAt + '\'' +
            ", pictures=" + pictures +
            ", city=" + city +
            ", notes=" + notes +
            ", etat=" + etat +
            ", user=" + user +
            ", panniers=" + panniers +
            ", category=" + category +
            '}';
    }
}
