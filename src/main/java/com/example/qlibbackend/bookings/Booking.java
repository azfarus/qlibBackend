package com.example.qlibbackend.bookings;

import javax.persistence.*;

import com.example.qlibbackend.books.Book;
import com.example.qlibbackend.members.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

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

    @OneToMany
    private Set<Member> Member;

    private Date reservationDate ;

    private Boolean status;


}
