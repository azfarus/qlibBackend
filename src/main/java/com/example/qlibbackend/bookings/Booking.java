package com.example.qlibbackend.bookings;

import javax.persistence.*;

import com.example.qlibbackend.books.Book;
import com.example.qlibbackend.members.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    private Long id;

    @OneToOne
    private Book book;

    @OneToOne
    private Member member;

    private Date reservationDate ;

    private Boolean status;


}
