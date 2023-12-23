package com.example.qlibbackend.borrowedbooks;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    List<Borrow> findAllByUserIdAndReturnDate(Long id, LocalDate date);
    List<Borrow> findAllByUserId(Long id);
}
