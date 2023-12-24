package com.example.qlibbackend.ebook;


import com.example.qlibbackend.file.File;
import com.example.qlibbackend.file.FileController;
import com.example.qlibbackend.file.FileStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @Autowired
    ObjectMapper mapper;

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
    private List<ObjectNode> all_ebook(){
        List<Ebook> all_ebook =ebookDB.findAll();
        List<ObjectNode> all_ebook_info = new ArrayList<>();
        for(Ebook x : all_ebook){
            File f = storageService.getFile(x.getPdfId());

            ObjectNode ebook_info=mapper.createObjectNode();

            ebook_info.put("ebookId" , x.getEbookId()); //use api in line 45 witt this ebook id to download ebook
            ebook_info.put("ebookFileName",f.getFilename()); // this is the file name
            ebook_info.put("ebookPhotoId",x.getPhotoId()); // this gives the associated cover photo, use /file/{id} api in filecontroller

            all_ebook_info.add(ebook_info);
        }


        return  all_ebook_info;
    }
}
