package com.example.qlibbackend.librarian;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LibrarianRepository extends JpaRepository<Librarian , String> {

}
