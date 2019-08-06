package com.web.poc1.service;

import com.web.poc1.exception.CustomException;
import com.web.poc1.model.ExcelRow;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface DocumentService {
    int uploadDocument(MultipartFile document) throws CustomException;

    List<ExcelRow> getRows();

    ExcelRow createRow();

    List<ExcelRow> updateRows(String rows);

    void deleteRows(Map<Integer, String> rows);

    List<ExcelRow> findByDynamicSelector(String platform, Pageable pageable);
}
