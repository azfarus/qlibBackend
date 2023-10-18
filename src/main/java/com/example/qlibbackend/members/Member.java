package com.example.qlibbackend.members;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String username;

    private String firstName;


    private String lastName;

    private Long contactNumber;

    private String email;

    private String password;

    private Long score;
}