package com.example.bibloltu;

import javafx.beans.property.SimpleStringProperty;

public class SearchView {

    private final SimpleStringProperty title;
    private final SimpleStringProperty type;
    private final SimpleStringProperty isbn;
    private final SimpleStringProperty genre;
    private final SimpleStringProperty location;
    private final SimpleStringProperty director_author;

    public SearchView(String title, String type, String genre, String isbn, String location, String director_author) {
        this.title = new SimpleStringProperty(title);
        this.type = new SimpleStringProperty(type);
        this.genre = new SimpleStringProperty(genre);
        this.isbn = new SimpleStringProperty(isbn);
        this.location = new SimpleStringProperty(location);
        this.director_author = new SimpleStringProperty(director_author);
    }

    public String getGenre() {
        return genre.get();
    }

    public SimpleStringProperty genreProperty() {
        return genre;
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public String getIsbn() {
        return isbn.get();
    }

    public void setIsbn(String value) {
        isbn.set(value);
    }

    public SimpleStringProperty isbnProperty() {
        return isbn;
    }

    public String getLocation() {
        return location.get();
    }

    public SimpleStringProperty locationProperty() {
        return location;
    }

    public SimpleStringProperty Director_AuthorProperty(){
        return director_author;
    }
}