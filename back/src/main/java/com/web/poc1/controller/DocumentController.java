package com.web.poc1.controller;

import com.web.poc1.exception.CustomException;
import com.web.poc1.model.ExcelRow;
import com.web.poc1.service.DocumentService;
import com.web.poc1.util.MessageHolder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.PathParam;


@RestController
@RequestMapping("/api/documents")
@AllArgsConstructor
public class DocumentController {

    DocumentService documentService;

    @PostMapping
    public ResponseEntity<MessageHolder> uploadDocument(@RequestParam("document") MultipartFile document) throws CustomException {

        documentService.uploadDocument(document);

        return new ResponseEntity<>(new MessageHolder("20 rows were added"), HttpStatus.CREATED);
    }

    @GetMapping
    public String getRows() {
        return "getting documents";
    }

    @PutMapping
    public String updateRow(@RequestBody ExcelRow row) {
        return "updating rows";
    }

    @DeleteMapping
    public String deleteRow(@PathParam("id") int id) {
        return "deleting row";
    }

}
