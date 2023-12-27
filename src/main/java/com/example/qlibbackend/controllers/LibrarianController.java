package com.example.qlibbackend.controllers;


import com.example.qlibbackend.authors.Author;
import com.example.qlibbackend.authors.AuthorRepository;
import com.example.qlibbackend.bookings.Booking;
import com.example.qlibbackend.bookings.BookingRepository;
import com.example.qlibbackend.books.Book;
import com.example.qlibbackend.books.BookRepository;
import com.example.qlibbackend.borrowedbooks.Borrow;
import com.example.qlibbackend.borrowedbooks.BorrowRepository;
import com.example.qlibbackend.departments.Genre;
import com.example.qlibbackend.departments.GenreRepository;
import com.example.qlibbackend.email.GmailEmailSender;
import com.example.qlibbackend.fines.FineRepository;
import com.example.qlibbackend.members.Member;
import com.example.qlibbackend.members.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;


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

    @Autowired
    FineRepository fineDB;

    @Autowired
    GenreRepository genreDB;

    @Autowired
    GmailEmailSender gmailEmailSender;

    @Autowired
    BookingRepository bookingDB;


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
                                              @RequestParam(required = false) Long authorid3,
                                              @RequestParam String genre,
                                                 @RequestParam Boolean isAcademic){

        if(bookDB.existsById(id)){





            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        if(!authorDB.existsById(authorid1)) authorid1 = null;
        if(authorid2 != null ) if(!authorDB.existsById(authorid2)) authorid2 = null;
        if(authorid3 != null ) if(!authorDB.existsById(authorid3)) authorid3 = null;
        genre=genre.trim();

        Optional<Genre> g = genreDB.findById(genre);
        if(g.isEmpty()){
            g= Optional.of(new Genre(genre, isAcademic, null));
            genreDB.save(g.get());
        }


        Optional<Booking> booking = bookingDB.findById(id);

        if(booking.isEmpty()){
            Booking b = new Booking(id,new HashSet<>() ,Instant.now() , "available");
            bookingDB.save(b);
        }

        Book newBook = new Book(id , title,isbn, year, subject , totalcopies , availablecopies , authorid1 , authorid2 , authorid3 , g.get() );
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
//      bookdata.put("title",b.get().getTitle());
//      bookdata.put("title",b.get().getTitle());

        return ResponseEntity.status(HttpStatus.OK).body(bookdata);

    }

    @GetMapping("/get-genres")
    @ResponseBody
    private ResponseEntity<List<String>> get_genres(){
        List<Genre> genres=genreDB.findAll();
        List<String> genreNames= new ArrayList<>();
        for(Genre x : genres){
            genreNames.add(x.getName());
        }

        return  ResponseEntity.status(HttpStatus.OK).body(genreNames);

    }

    @GetMapping("/get-authors")
    @ResponseBody
    private ResponseEntity<List<ObjectNode>> get_authors(@RequestParam String genreName){

        Optional<Genre> g =genreDB.findById(genreName);
        if(g.isEmpty()) ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        List<Book> booksOfGenre=bookDB.findBooksByBookGenre(g.get());
        Set<Long> authorIds = new HashSet<>();

        for(Book b: booksOfGenre){
            if(b.getAuthorId1() != null) authorIds.add(b.getAuthorId1());
            if(b.getAuthorId2() != null) authorIds.add(b.getAuthorId2());
            if(b.getAuthorId3() != null) authorIds.add(b.getAuthorId3());
        }

        List<ObjectNode> authorDeets = new ArrayList<>();

        for(Long authorId : authorIds){
            Optional<Author> a = authorDB.findById(authorId);
            if(a.isPresent()){
                ObjectNode o = mapper.createObjectNode();
                o.put("id" , a.get().getId());
                o.put("name",a.get().getName());
                authorDeets.add(o);
            }
        }

        return  ResponseEntity.status(HttpStatus.OK).body(authorDeets);

    }

    @GetMapping("/get-books")
    @ResponseBody
    private ResponseEntity<Set<ObjectNode>> get_books(@RequestParam String genreName , @RequestParam Long authorId){

        genreName.trim();
        Optional<Genre> g = genreDB.findById(genreName);
        if(g.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        List<Book> auth1match=bookDB.findBooksByBookGenreAndAuthorId1(g.get(),authorId);
        List<Book> auth2match=bookDB.findBooksByBookGenreAndAuthorId2(g.get(),authorId);
        List<Book> auth3match=bookDB.findBooksByBookGenreAndAuthorId3(g.get(),authorId);

        Set<ObjectNode> result = new HashSet<>();

        for(Book b : auth1match){



            ObjectNode bookdata = mapper.createObjectNode();
            bookdata.put("title",b.getTitle());
            bookdata.put("isbn",b.getISBN());
            bookdata.put("publishedon",b.getPublishedOn());
            bookdata.put("bookid",b.getId());
            bookdata.put("totalcopies",b.getTotalCopies());
            bookdata.put("availablecopies",b.getAvailableCopies());
            result.add(bookdata);
        }
        for(Book b : auth2match){

            ObjectNode bookdata = mapper.createObjectNode();
            bookdata.put("title",b.getTitle());
            bookdata.put("isbn",b.getISBN());
            bookdata.put("publishedon",b.getPublishedOn());
            bookdata.put("bookid",b.getId());
            bookdata.put("totalcopies",b.getTotalCopies());
            bookdata.put("availablecopies",b.getAvailableCopies());
            result.add(bookdata);
        }
        for(Book b : auth3match){

            ObjectNode bookdata = mapper.createObjectNode();
            bookdata.put("title",b.getTitle());
            bookdata.put("isbn",b.getISBN());
            bookdata.put("publishedon",b.getPublishedOn());
            bookdata.put("bookid",b.getId());
            bookdata.put("totalcopies",b.getTotalCopies());
            bookdata.put("availablecopies",b.getAvailableCopies());
            result.add(bookdata);
        }

        return  ResponseEntity.status(HttpStatus.OK).body(result);

    }



    @PostMapping("/book-borrow/{memberid}")
    @ResponseBody
    private ResponseEntity<String> book_borrow(@PathVariable Long memberid , @RequestParam List<Long> bookids){


        long seconds = 30;


        for(Long x : bookids){
            Optional<Book> book = bookDB.findById(x);

            if(book.isEmpty()) continue;
            if(book.get().getAvailableCopies() <= 0) continue;

            book.get().setAvailableCopies(book.get().getAvailableCopies()-1);
            Instant cur = Instant.now();
            Instant due = cur.plusSeconds(seconds);

            Optional<Booking> booking = bookingDB.findById(book.get().getId());

            if(booking.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("HEHE");
            }

            if(book.get().getAvailableCopies() == 0){
                booking.get().setStatus("unavailable");
            }

            bookingDB.save(booking.get());

            Borrow b = new Borrow(null ,x , memberid , cur , null , due);
            borrowDB.save(b);
        }

       return ResponseEntity.status(HttpStatus.OK).body("HEHE");
    }

    @GetMapping("/book-borrow-deets/{memberid}")
    @ResponseBody
    private ResponseEntity<List<Borrow>> book_borrow_data(@PathVariable Long memberid ){



        return ResponseEntity.status(HttpStatus.OK).body(borrowDB.findAllByUserIdAndReturnDate(memberid,null));
    }

    @PostMapping("/book-return/{borrowid}")
    @ResponseBody
    private ResponseEntity<List<Borrow>> return_book(@PathVariable Long borrowid ){


        if(borrowDB.existsById(borrowid)){
            Optional<Borrow> b = borrowDB.findById(borrowid);
            b.get().setReturnDate(Instant.now());
            borrowDB.save(b.get());



            Long bookId = b.get().getBookId();
            Optional<Booking> booking = bookingDB.findById(bookId);
            Optional<Book> book = bookDB.findById(bookId);

            if(book.isEmpty() || booking.isEmpty()) ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);


            book.get().setAvailableCopies(book.get().getAvailableCopies()+1);
            bookDB.save(book.get());

            booking.get().setStatus("available");
            bookingDB.save(booking.get());





            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    @GetMapping("/fine-deets/{borrowid}")
    @ResponseBody
    private ResponseEntity<Float> fine_data(@PathVariable Long borrowid ){

        if(fineDB.existsById(borrowid)){
            return ResponseEntity.status(HttpStatus.OK).body(fineDB.findById(borrowid).get().getAmount());
        }


        return ResponseEntity.status(HttpStatus.OK).body((float)0.00);
    }


    @PostMapping("/member-registration")
    @ResponseBody
    private ResponseEntity<String> registerMember(@RequestParam String name,
                                                  @RequestParam Long memberid,
                                                  @RequestParam String email,
                                                  @RequestParam Long contactNumber,
                                                  @RequestParam String username) throws Exception {
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
        String message= "Congratulations, you are a member!\nUsername: "+username+"\nPassword: "+generatedPassword;
        gmailEmailSender.sendEmail(email, "Qlibrary Registration" ,message);
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
