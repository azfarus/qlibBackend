package com.example.qlibbackend.books;

import com.example.qlibbackend.authors.Author;
import com.example.qlibbackend.departments.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findBooksByBookGenre(Genre genre);
    List<Book> findBooksByBookGenreAndAuthorId1(Genre g , Long id);
    List<Book> findBooksByBookGenreAndAuthorId2(Genre g , Long id);
    List<Book> findBooksByBookGenreAndAuthorId3(Genre g , Long id);
}
