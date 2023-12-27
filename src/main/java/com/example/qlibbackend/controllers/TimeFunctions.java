package com.example.qlibbackend.controllers;


import com.example.qlibbackend.bookings.Booking;
import com.example.qlibbackend.bookings.BookingRepository;
import com.example.qlibbackend.books.Book;
import com.example.qlibbackend.books.BookRepository;
import com.example.qlibbackend.borrowedbooks.Borrow;
import com.example.qlibbackend.borrowedbooks.BorrowRepository;
import com.example.qlibbackend.email.GmailEmailSender;
import com.example.qlibbackend.fines.Fine;
import com.example.qlibbackend.fines.FineRepository;
import com.example.qlibbackend.members.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class TimeFunctions {


    @Autowired
    FineRepository fineDB;

    @Autowired
    BookRepository bookDB;

    @Autowired
    BorrowRepository borrowDB;

    @Autowired
    BookingRepository bookingDB;

    @Autowired
    GmailEmailSender gmailEmailSender;
    @Scheduled(fixedRate = 20*1000)
    private void update_fines(){
        List<Borrow> all_borrows=borrowDB.findAll();
        System.out.println("Updating fine table" + all_borrows.size());

        float cost_factor = 15;


        for(Borrow borrow : all_borrows){

            Duration duration = Duration.between( borrow.getDueDate(),Instant.now() );
            if(borrow.getReturnDate() == null && (duration.getSeconds() > 0 )){

                float amount = duration.getSeconds() * cost_factor;
                Fine fine = new Fine(borrow.getId(), amount , Instant.now(),false);
                fineDB.save(fine);
            }
        }
    }

    @Scheduled(fixedRate = 10*1000 , initialDelay = 10*1000)
    private void notify_availability() throws Exception {
        List<Booking> all_bookings = bookingDB.findAll();

        for(Booking booking : all_bookings){

            Optional<Book> book = bookDB.findById(booking.getBookid());

            if(book.isEmpty()) continue;
            if(booking.getStatus().equals("available")){
                for(Member member : booking.getMember()){
                    String bookname = book.get().getTitle();
                    String message = bookname +" is now available for borrowing in Qlibrary.";
                    System.out.println(message);
                    gmailEmailSender.sendEmail(member.getEmail() , bookname , message);

                }
                booking.setStatus("notified");
                bookingDB.save(booking);
            }

        }
    }
}
