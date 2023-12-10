package com.example.qlibbackend.departments;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.example.qlibbackend.books.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    @Id
    private String name;

    private Boolean isAcademic;
    private Long shelfNo;

//    @OneToMany
//    private List<Book> books;
}
