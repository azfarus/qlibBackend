package com.example.qlibbackend.bookings;

import javax.persistence.*;

import com.example.qlibbackend.books.Book;
import com.example.qlibbackend.members.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
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
    private Long bookid;

    @OneToMany(mappedBy = "booking")
    private Set<Member> member;

    private Instant reservationDate ;

    private Boolean status;


}
