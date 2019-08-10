package com.web.poc1.controller;

import com.web.poc1.exception.CustomException;
import com.web.poc1.model.ExcelRow;
import com.web.poc1.service.DocumentService;
import com.web.poc1.to.FindRequestTo;
import com.web.poc1.util.MessageHolder;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value = "/row")
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

    @GetMapping
    public ResponseEntity<FindRequestTo> getDynamic(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                    @RequestParam(value = "platform", required = false) String platform,
                                                    @RequestParam(value = "unit", required = false) Integer unit,
                                                    @RequestParam(value = "account", required = false) Integer account,
                                                    @RequestParam(value = "date", required = false) String date,
                                                    @RequestParam(value = "amount", required = false) Double amount) throws CustomException {
        return new ResponseEntity<>(this.documentService.findByDynamicSelector(platform, unit, account, date, amount,
                PageRequest.of((page > 0) ? page - 1 : 0, 5)),
                HttpStatus.OK);
    }

}
