package com.example.qlibbackend.ebook;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ebook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ebookId;

    private Long pdfId;
    private Long photoId;
}
