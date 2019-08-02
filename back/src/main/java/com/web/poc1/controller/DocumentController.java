package com.web.poc1.controller;

import com.google.gson.JsonArray;
import com.web.poc1.exception.CustomException;
import com.web.poc1.model.ExcelRow;
import com.web.poc1.service.DocumentService;
import com.web.poc1.util.MessageHolder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
@AllArgsConstructor
public class DocumentController {

    DocumentService documentService;

    @PostMapping
    public ResponseEntity<MessageHolder> uploadDocument(@RequestParam("document") MultipartFile document) throws CustomException {
        int numberOfRows = documentService.uploadDocument(document);
        return new ResponseEntity<>(new MessageHolder(numberOfRows + " rows were added"), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExcelRow>> getRows() {
        return new ResponseEntity<>(documentService.getRows(), HttpStatus.OK);
    }

    @PostMapping(value="/row")
    public ResponseEntity<ExcelRow> createRow() {
        return new ResponseEntity<>(documentService.createRow(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity updateRows(@RequestBody String rows) {
        this.documentService.updateRows(rows);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/delete/rows")
    public ResponseEntity deleteRows(@RequestBody Map<Integer, String> rows) {
        this.documentService.deleteRows(rows);
        return new ResponseEntity(HttpStatus.OK);
    }

}
