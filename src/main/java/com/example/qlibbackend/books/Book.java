package com.example.qlibbackend.books;

import javax.persistence.*;

import com.example.qlibbackend.bookings.Booking;
import com.example.qlibbackend.departments.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Year;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {


    @Id
    private Long id;

    private String title;



    private  Long ISBN;

    private  String publishedOn;

    private String subject;

    private Long totalCopies;

    private Long availableCopies;

    private Long authorId1;

    private Long authorId2;

    private Long authorId3;

    @ManyToOne
    private Genre bookGenre;


    @OneToOne
    private Booking booking;
}
