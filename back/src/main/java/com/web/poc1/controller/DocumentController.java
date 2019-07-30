package com.web.poc1.controller;

import com.web.poc1.model.Row;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PathParam;


@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @PostMapping
    @ResponseBody
    public String uploadDocument() {
        return "uploading excel";
    }

    @GetMapping
    @ResponseBody
    public String getRows() {
        return "getting documents";
    }

    @PutMapping
    @ResponseBody
    public String updateRow(@RequestBody Row row) {
        return "updating rows";
    }

    @DeleteMapping
    @ResponseBody
    public String deleteRow(@PathParam("id") int id) {
        return "deleting row";
    }

}
