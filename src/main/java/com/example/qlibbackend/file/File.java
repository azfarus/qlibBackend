package com.example.qlibbackend.file;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy =GenerationType.AUTO)
    private Long id;

    private String filename;
    private String filetype;

    private Timestamp ts;

    @Lob
    private byte[] data;






    public File(String filename, String filetype, byte[] data) {
        Date det = new Date();

        this.ts = new Timestamp(det.getTime());

        this.filename = filename;
        this.filetype = filetype;
        this.data = data;
    }
}