package com.example.qlibbackend.bookings;

import com.example.qlibbackend.books.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking , Long > {


}
