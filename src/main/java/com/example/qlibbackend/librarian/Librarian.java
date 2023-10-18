package com.example.qlibbackend.librarian;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Librarian {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String username;

    private String firstName;


    private String lastName;

    private Long contactNumber;

    private String email;

    private String password;
}