package com.alura.literalura.repository;

import com.alura.literalura.model.AuthorInfo;
import com.alura.literalura.model.Book;
import com.alura.literalura.model.Languages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT a FROM Book b JOIN b.authors a")
    List<AuthorInfo> getAuthorsInfo ();
    
    @Query("SELECT a FROM Book b JOIN b.authors a WHERE birthYear > :date")
    List<AuthorInfo> getAuthorLiveAfter ( Integer date );

    
    List<Book> findByLanguages ( Languages languages );
}
