package com.example.qlibbackend.borrowedbooks;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Borrow {
    @Id
    private Long id;

    private  Long bookId;

    private  String membrUsrname;

    private Date borrowDate , returnDate , dueDate ;
}
