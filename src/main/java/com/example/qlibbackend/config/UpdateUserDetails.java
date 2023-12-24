package com.example.qlibbackend.config;


import com.example.qlibbackend.librarian.Librarian;
import com.example.qlibbackend.librarian.LibrarianRepository;
import com.example.qlibbackend.members.Member;
import com.example.qlibbackend.members.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateUserDetails {
    @Autowired
    InMemoryUserDetailsManager userDetailsManager;


    @Autowired
    LibrarianRepository librarianDB;

    @Autowired
    MemberRepository memberDB;

    public PasswordEncoder passwordEncoder() {
        // Use BCryptPasswordEncoder for password hashing
        return new BCryptPasswordEncoder();
    }
    @Scheduled(fixedDelay = 1000 ,initialDelay = 3000) // Run every hour (adjust the delay as needed)
    public void updateUserDetails() {
        List<UserDetails> allUsers = new ArrayList<>();

        // Retrieve librarians and add them to userDetails
        List<Librarian> allLibrarians = librarianDB.findAll();
        for (Librarian x : allLibrarians) {
            UserDetails user = User.builder()
                    .username(x.getUsername())
                    .password(passwordEncoder().encode(x.getPassword())) // Encode the password
                    .roles("LIBRARIAN")
                    .build();

            //allUsers.add(user);
            if(!userDetailsManager.userExists(user.getUsername())) userDetailsManager.createUser(user);
        }

        // Retrieve members and add them to userDetails
        List<Member> allMembers = memberDB.findAll();
        for (Member x : allMembers) {
            UserDetails user = User.builder()
                    .username(x.getUsername())
                    .password(passwordEncoder().encode(x.getPassword())) // Encode the password
                    .roles("STUDENT")
                    .build();

            //allUsers.add(user);
            if(!userDetailsManager.userExists(user.getUsername())) userDetailsManager.createUser(user);
        }

    }
}
