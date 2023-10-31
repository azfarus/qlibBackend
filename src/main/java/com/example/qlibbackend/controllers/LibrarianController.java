package com.example.qlibbackend.controllers;


import com.example.qlibbackend.authors.Author;
import com.example.qlibbackend.authors.AuthorRepository;
import com.example.qlibbackend.books.Book;
import com.example.qlibbackend.books.BookRepository;
import com.example.qlibbackend.borrowedbooks.Borrow;
import com.example.qlibbackend.borrowedbooks.BorrowRepository;
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
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/librarian")
public class LibrarianController {


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


    @PostMapping("/book-entry")
    @ResponseBody
    private ResponseEntity<String> book_entry(@RequestParam String title,
                                              @RequestParam Long id,
                                              @RequestParam Long isbn,
                                              @RequestParam String year,
                                              @RequestParam String subject,
                                              @RequestParam Long totalcopies,
                                              @RequestParam Long availablecopies,
                                              @RequestParam Long authorid1,
                                              @RequestParam(required = false) Long authorid2,
                                              @RequestParam(required = false) Long authorid3){

        if(!authorDB.existsById(authorid1)) authorid1 = null;
        if(authorid2 != null ) if(!authorDB.existsById(authorid2)) authorid2 = null;
        if(authorid3 != null ) if(!authorDB.existsById(authorid3)) authorid3 = null;

        Book newBook = new Book(id , title,isbn, year, subject , totalcopies , availablecopies , authorid1 , authorid2 , authorid3);
        bookDB.save(newBook);
        return  ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/author-entry")
    @ResponseBody
    private ResponseEntity<String> author_entry(@RequestParam Long id,
                                                @RequestParam String name,
                                                @RequestParam String nationality
    ){

        Author newAuthor = new Author(id , name , nationality);

        authorDB.save(newAuthor);

        return  ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/book-info/{bookid}")
    @ResponseBody
    private ResponseEntity<ObjectNode> get_bookinfo(@PathVariable Long bookid){

        ObjectNode bookdata = mapper.createObjectNode();

        Optional<Book> b = bookDB.findById(bookid);

        if(b.isEmpty()) ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        bookdata.put("title",b.get().getTitle());
        bookdata.put("isbn",b.get().getISBN());
        bookdata.put("publishedon",b.get().getPublishedOn());
        bookdata.put("bookid",b.get().getId());
//        bookdata.put("title",b.get().getTitle());
//        bookdata.put("title",b.get().getTitle());

        return ResponseEntity.status(HttpStatus.OK).body(bookdata);

    }


    @PostMapping("/book-borrow/{memberid}")
    @ResponseBody
    private ResponseEntity<String> book_borrow(@PathVariable Long memberid , @RequestParam List<Long> bookids){

        for(Long x : bookids){
            Optional<Book> book = bookDB.findById(x);

            if(book.isEmpty()) continue;
            if(book.get().getAvailableCopies() <= 0) continue;

            book.get().setAvailableCopies(book.get().getAvailableCopies()-1);
            LocalDate cur = LocalDate.now();
            LocalDate due = cur.plusDays(14);
            Borrow b = new Borrow(null ,x , memberid , cur , null , due);
            borrowDB.save(b);
        }

       return ResponseEntity.status(HttpStatus.OK).body("HEHE");
    }


    @PostMapping("/member-registration")
    @ResponseBody
    private ResponseEntity<String> registerMember(@RequestParam String name,
                                                  @RequestParam Long memberid,
                                                  @RequestParam String email,
                                                  @RequestParam Long contactNumber,
                                                  @RequestParam String username) {
        // Check if the username is already taken
        if (memberDB.existsById(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists.");
        }

        // Generate a random password
        String generatedPassword = generateRandomPassword();

        // Create a new member
        Member newMember = new Member();
        newMember.setName(name);
        newMember.setMemberid(memberid);
        newMember.setEmail(email);
        newMember.setContactNumber(contactNumber);
        newMember.setUsername(username);
        newMember.setPassword(generatedPassword);

        // Save the member to the database
        memberDB.save(newMember);

        // You can implement sending an email or other notification mechanism here

        return ResponseEntity.status(HttpStatus.OK).body("Member registered successfully. Generated password: " + generatedPassword);
    }

    private String generateRandomPassword() {
        // Define the characters to use for generating the password
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";

        // Define the length of the password
        int length = 10; // You can adjust the length as needed

        // Create a StringBuilder to build the password
        StringBuilder password = new StringBuilder();

        // Generate the random password
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }

}
