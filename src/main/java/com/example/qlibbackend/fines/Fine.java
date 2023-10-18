package com.example.qlibbackend.fines;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Year;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String membrUsername;

    private  Long borrowId;

    private Float amount;

    private Date created;

    private Boolean payment;
}