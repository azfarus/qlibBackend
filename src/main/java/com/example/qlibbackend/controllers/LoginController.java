package com.example.qlibbackend.controllers;

import com.example.qlibbackend.hasher.StringHasher;
import com.example.qlibbackend.librarian.Librarian;
import com.example.qlibbackend.librarian.LibrarianRepository;
import com.example.qlibbackend.members.Member;
import com.example.qlibbackend.members.MemberRepository;
import com.example.qlibbackend.session.Session;
import com.example.qlibbackend.session.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.Instant;
import java.time.LocalTime;
import java.util.Optional;

@RestController
@RequestMapping("/login")
public class LoginController {


    @Autowired
    LibrarianRepository librarianDB;

    @Autowired
    MemberRepository memberDB;

    @Autowired
    SessionRepository sessionDB;

    @PostMapping("/librarian")
    @ResponseBody
    public ResponseEntity<String> librarian_login(@RequestParam String username , @RequestParam String password){
        Optional<Librarian> libguy = librarianDB.findById(username);

        if(libguy.isPresent() && libguy.get().getPassword().equals(password)){
            return ResponseEntity.status(HttpStatus.OK).body(username);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("FALSE");
    }


    @CrossOrigin
    @PostMapping("/member")
    @ResponseBody
    public ResponseEntity<String> student_login(@RequestParam String username , @RequestParam String password){
        Optional<Member> guy = memberDB.findById(username);

        if(guy.isPresent() && guy.get().getPassword().equals(password)){

            String sessString = username + password + Instant.now().toString();

            String  hashString = StringHasher.hashString(sessString);
            Session s = new Session(hashString , Instant.now().plusSeconds(300));
            sessionDB.save(s);
            return ResponseEntity.status(HttpStatus.OK).body(hashString);

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("FALSE");
    }



}