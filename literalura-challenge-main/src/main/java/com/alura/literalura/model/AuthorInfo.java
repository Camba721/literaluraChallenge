package com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "authors")
public class AuthorInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer birthYear;
    private Integer deathYear;
    @ManyToOne
    private Book book;
    
    public AuthorInfo () {}
    
    public AuthorInfo( AuthorInfo authorInfo ){}
    
    public AuthorInfo(String name, Integer birthYear, Integer deathYear, Book book) {
        this.name = name;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
        this.book = book;
    }

    
    public Book getBook () {
        return book;
    }
    
    public void setBook ( Book book ) {
        this.book = book;
    }
    
    public Integer getDeathYear () {
        return deathYear;
    }
    
    public void setDeathYear ( Integer deathYear ) {
        this.deathYear = deathYear;
    }
    
    public Integer getBirthYear () {
        return birthYear;
    }
    
    public void setBirthYear ( Integer birthYear ) {
        this.birthYear = birthYear;
    }
    
    public String getName () {
        return name;
    }
    
    public void setName ( String name ) {
        this.name = name;
    }
    
    public Long getId () {
        return id;
    }
    
    public void setId ( Long id ) {
        this.id = id;
    }
    
    @Override
    public String toString () {
        return
                "name='" + name + '\'' +
                ", birthYear=" + birthYear +
                ", deathYear=" + deathYear;
    }
}
