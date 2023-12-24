package com.example.qlibbackend.controllers;


import com.example.qlibbackend.borrowedbooks.Borrow;
import com.example.qlibbackend.borrowedbooks.BorrowRepository;
import com.example.qlibbackend.fines.Fine;
import com.example.qlibbackend.fines.FineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Component
public class TimeFunctions {


    @Autowired
    FineRepository fineDB;

    @Autowired
    BorrowRepository borrowDB;
    @Scheduled(fixedRate = 20*1000)
    private void update_fines(){
        List<Borrow> all_borrows=borrowDB.findAll();
        System.out.println("Updating fine table" + all_borrows.size());

        float cost_factor = 15;


        for(Borrow borrow : all_borrows){

            Duration duration = Duration.between( borrow.getDueDate(),Instant.now() );
            if(borrow.getReturnDate() == null && (duration.getSeconds() > 0 )){

                float amount = duration.getSeconds() * cost_factor;
                Fine fine = new Fine(borrow.getId(), amount , Instant.now(),false);
                fineDB.save(fine);
            }
        }
    }
}
