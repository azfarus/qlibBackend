package com.example.qlibbackend.members;

import javax.persistence.*;
;
import com.example.qlibbackend.bookings.Booking;
import com.example.qlibbackend.fines.Fine;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    private String username;

    private Long memberid;

    private String name;

    private Long contactNumber;

    private String email;

    private String password;

    private Long score;

    @OneToOne
    private Fine fine;

    @ManyToOne
    private Booking booking;




}