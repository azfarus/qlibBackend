package com.example.qlibbackend.fines;

import javax.persistence.*;

import com.example.qlibbackend.members.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fine {
    @Id
    private  Long borrowId;


    private Float amount;

    private Instant created;

    private Boolean payment;
}