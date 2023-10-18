package com.example.qlibbackend.controllers;


import com.example.qlibbackend.authors.Author;
import com.example.qlibbackend.authors.AuthorRepository;
import com.example.qlibbackend.books.Book;
import com.example.qlibbackend.books.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;

@RestController
@RequestMapping("/librarian")
public class LibrarianController {


    @Autowired
    BookRepository bookDB;

    @Autowired
    AuthorRepository authorDB;


    @PostMapping("/book-entry")
    @ResponseBody
    private ResponseEntity<String> book_entry(@RequestParam String title,
                                              @RequestParam Long isbn,
                                              @RequestParam String year,
                                              @RequestParam String subject,
                                              @RequestParam Long totalcopies,
                                              @RequestParam Long availablecopies,
                                              @RequestParam Long authorid1,
                                              @RequestParam Long authorid2,
                                              @RequestParam Long authorid3){

        if(!authorDB.existsById(authorid1)) authorid1 = null;
        if(!authorDB.existsById(authorid2)) authorid2 = null;
        if(!authorDB.existsById(authorid3)) authorid3 = null;

        Book newBook = new Book(null , title,isbn, year, subject , totalcopies , availablecopies , authorid1 , authorid2 , authorid3);
        bookDB.save(newBook);
        return  ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/author-entry")
    @ResponseBody
    private ResponseEntity<String> author_entry(@RequestParam String name,
                                              @RequestParam String nationality
                                              ){

        Author newAuthor = new Author(null , name , nationality);

        authorDB.save(newAuthor);

        return  ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
