package com.example.qlibbackend.members;

import javax.persistence.*;
;
import com.example.qlibbackend.bookings.Booking;
import com.example.qlibbackend.fines.Fine;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;

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

    @JsonIgnore
    private String password;

    private Long score;

    @OneToOne
    @JsonIgnore
    private Fine fine;

    @ManyToOne
    @JsonIgnore
    private Booking booking;





}