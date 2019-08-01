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

import java.util.List;

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

    @PutMapping
    public ResponseEntity<ExcelRow> updateRow(@RequestBody ExcelRow row) {
        return new ResponseEntity<>(documentService.updateRow(row), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteRow(@PathVariable Long id) {
        documentService.deleteRow(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
