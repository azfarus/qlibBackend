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
import com.example.qlibbackend.fines.Fine;
import com.example.qlibbackend.fines.FineRepository;
import com.example.qlibbackend.members.Member;
import com.example.qlibbackend.members.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    BookingRepository bookingDB;

    @GetMapping("/member-info/{username}")
    @ResponseBody
    public ResponseEntity<Member> getMemberByUsername(@PathVariable String username) {
        Optional<Member> memberOptional = memberDB.findMemberByUsername(username);

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            return ResponseEntity.status(HttpStatus.OK).body(member);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/update-member/{username}")
    @ResponseBody
    public ResponseEntity<Member> updateMemberInfo(
            @PathVariable String username,
            @RequestBody Member updatedMember) {

        Optional<Member> existingMemberOptional = memberDB.findMemberByUsername(username);

        if (existingMemberOptional.isPresent()) {
            Member existingMember = existingMemberOptional.get();

            // Update email and phone number fields using Lombok-generated setters
            existingMember.setEmail(updatedMember.getEmail());
            existingMember.setContactNumber(updatedMember.getContactNumber());

            // Save the updated member to the database
            memberDB.save(existingMember);

            return ResponseEntity.status(HttpStatus.OK).body(existingMember);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }



    @GetMapping("/get-all-books")
    @ResponseBody
    private ResponseEntity<List<ObjectNode>> get_all_books() {
        List<ObjectNode> all_book_list = new ArrayList<>();

        // Retrieve all books from the database
        List<Book> allBooks = bookDB.findAll();

        for (Book book : allBooks) {
            ObjectNode o = mapper.createObjectNode();

            // Populate the ObjectNode with book information
            o.put("id", book.getId());
            o.put("title", book.getTitle());
            o.put("ISBN", book.getISBN());
            o.put("publishedOn", book.getPublishedOn());
            o.put("subject", book.getSubject());
            o.put("totalCopies", book.getTotalCopies());
            o.put("availableCopies", book.getAvailableCopies());

            // Assuming you want to include author names
            List<String> authorNames = new ArrayList<>();
            addAuthorNameIfNotNull(authorNames, book.getAuthorId1());
            addAuthorNameIfNotNull(authorNames, book.getAuthorId2());
            addAuthorNameIfNotNull(authorNames, book.getAuthorId3());
            o.put("authors", String.join(", ", authorNames));

            o.put("genre", book.getBookGenre().getName());

            all_book_list.add(o);
        }

        return ResponseEntity.status(HttpStatus.OK).body(all_book_list);
    }

    private void addAuthorNameIfNotNull(List<String> authorNames, Long authorId) {
        if (authorId != null) {
            Optional<Author> author = authorDB.findById(authorId);
            author.ifPresent(value -> authorNames.add(value.getName()));
        }
    }


    private static String instantToString(Instant instant) {
        // Convert Instant to LocalDate in UTC timezone
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("UTC"));
        return formatter.format(instant);
    }

    @GetMapping("/get-all-borrow")
    @ResponseBody
    private ResponseEntity<List<ObjectNode>> get_all_borrow(@RequestParam String username){
        List<ObjectNode> all_order_list = new ArrayList<>();

        Optional<Member> m = memberDB.findMemberByUsername(username);

        if(m.isEmpty()) return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);


        List<Borrow> all_borrows = borrowDB.findAllByUserId(m.get().getMemberid());


        for(Borrow b : all_borrows){


            if(b.getReturnDate() != null) continue;
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


            Optional<Fine> fine = fineDB.findById(b.getId());
            Duration duration = Duration.between( b.getDueDate(),Instant.now() );

            o.put("id" , b.getId());
            o.put("genre" ,book.get().getBookGenre().getName());
            o.put("title",book.get().getTitle());

            o.put("author", authornames);
            o.put("borrowedDeadline",instantToString(b.getDueDate()));
            o.put("reservation_date",instantToString(b.getBorrowDate()));
            o.put("overdue" ,"0" );
            if( duration.getSeconds() > 0 ){
                Long days = duration.getSeconds()/86400;

                o.put("overdue" , days);
            }
            o.put("fine" , "0.0");
            fine.ifPresent(value -> o.put("fine", value.getAmount().toString()));


            all_order_list.add(o);



        }

        return ResponseEntity.status(HttpStatus.OK).body(all_order_list);

    }


    @PostMapping("/reserve")
    @ResponseBody
    private ResponseEntity<Long> reserve_book(@RequestParam String username , @RequestParam Long bookid){

        Optional<Booking> booking = bookingDB.findById(bookid);
        Optional<Book> book = bookDB.findById(bookid);
        Optional<Member> member = memberDB.findMemberByUsername(username);


        if(booking.isEmpty() || book.isEmpty() || member.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((long)-1);
        if(book.get().getAvailableCopies() > 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((long)-2);



        member.get().setBooking(booking.get());

        memberDB.save(member.get());



        return ResponseEntity.status(HttpStatus.OK).body((long)-1);

    }






}
