package com.example.qlibbackend.controllers;




import com.example.qlibbackend.authors.Author;
import com.example.qlibbackend.authors.AuthorRepository;
import com.example.qlibbackend.books.Book;
import com.example.qlibbackend.books.BookRepository;
import com.example.qlibbackend.borrowedbooks.Borrow;
import com.example.qlibbackend.borrowedbooks.BorrowRepository;
import com.example.qlibbackend.departments.Genre;
import com.example.qlibbackend.departments.GenreRepository;
import com.example.qlibbackend.fines.FineRepository;
import com.example.qlibbackend.members.Member;
import com.example.qlibbackend.members.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Year;
import java.util.*;



@CrossOrigin
@RestController
@RequestMapping("/student")
public class StudentController {


    @Autowired
    BookRepository bookDB;

    @Autowired
    AuthorRepository authorDB;

    @Autowired
    BorrowRepository borrowDB;

    @Autowired
    MemberRepository memberDB;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    FineRepository fineDB;

    @Autowired
    GenreRepository genreDB;

    @GetMapping("/get-all-books")
    @ResponseBody
    private ResponseEntity<List<ObjectNode>> get_all_books(){
        List<ObjectNode> all_book_list = new ArrayList<>();
        bookDB.findAll();

        return  ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/get-all-borrow")
    @ResponseBody
    private ResponseEntity<List<ObjectNode>> get_all_borrow(@RequestParam String username){
        List<ObjectNode> all_order_list = new ArrayList<>();

        Optional<Member> m = memberDB.findMemberByUsername(username);

        if(m.isEmpty()) return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);


        List<Borrow> all_borrows = borrowDB.findAllByUserId(m.get().getMemberid());


        for(Borrow b : all_borrows){
            ObjectNode o = mapper.createObjectNode();

            Optional<Book> book= bookDB.findById(b.getBookId());
            if(book.isEmpty()) return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            String authornames = "";

           if(book.get().getAuthorId1() != null){
               Optional<Author> a = authorDB.findById(book.get().getAuthorId1());
               if(a.isEmpty()) return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
               authornames+=a.get().getName();
               authornames+=", ";
           }
            if(book.get().getAuthorId2() != null){
                Optional<Author> a = authorDB.findById(book.get().getAuthorId2());
                if(a.isEmpty()) return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                authornames+=a.get().getName();
                authornames+=", ";
            }

            if(book.get().getAuthorId3() != null){
                Optional<Author> a = authorDB.findById(book.get().getAuthorId3());
                if(a.isEmpty()) return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                authornames+=a.get().getName();
                authornames+=", ";
            }

            o.put("id" , b.getId());
            o.put("genre" ,book.get().getBookGenre().getName());
            o.put("title",book.get().getTitle());

            o.put("author", authornames);
            o.put("borrowedDeadline",b.getDueDate().toString());
            o.put("reservation_date",b.getBorrowDate().toString());

            all_order_list.add(o);



        }

        return ResponseEntity.status(HttpStatus.OK).body(all_order_list);

    }







}
