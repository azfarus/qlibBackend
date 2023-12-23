package com.example.qlibbackend.ebook;


import com.example.qlibbackend.file.File;
import com.example.qlibbackend.file.FileController;
import com.example.qlibbackend.file.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    EbookRepository ebookDB;

    @Autowired
    FileStorageService storageService;

    @PostMapping("/create")
    @ResponseBody
    private Long create_ebook(@RequestParam Long fileId , @RequestParam Long photoId){
        Ebook eb = new Ebook(null , fileId , photoId);
        ebookDB.save(eb);


        return  (long)1;


    }

    @GetMapping("/get/{ebookid}")
    @ResponseBody
    private ResponseEntity<byte[]> get_ebook(@PathVariable Long ebookid){
        Optional<Ebook> eb = ebookDB.findById(ebookid);

        if(eb.isEmpty()) return  ResponseEntity.status(HttpStatus.OK).body(null);

        File fileDB = storageService.getFile(eb.get().getPdfId());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getFilename() + "\"")
                .body(fileDB.getData());


    }

    @GetMapping("/get/allebooks")
    @ResponseBody
    private List<Ebook> all_ebook(){
        return  ebookDB.findAll();
    }
}
