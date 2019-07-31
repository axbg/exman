package com.web.poc1.service;

import com.web.poc1.exception.CustomException;
import com.web.poc1.model.ExcelRow;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    int uploadDocument(MultipartFile document) throws CustomException;

    List<ExcelRow> getRows();

    ExcelRow updateRow(ExcelRow row);

    void deleteRow(Long id);
}
