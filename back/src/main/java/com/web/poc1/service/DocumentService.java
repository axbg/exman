package com.web.poc1.service;

import com.web.poc1.exception.CustomException;
import com.web.poc1.model.ExcelRow;
import com.web.poc1.to.FindRequestTo;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

public interface DocumentService {
    int uploadDocument(MultipartFile document) throws CustomException;

    FindRequestTo findByDynamicSelector(String platform, boolean meta, Integer unit, Integer account, String date, Double amount, Pageable pageable);

    ExcelRow createRow();

    FindRequestTo updateRows(String rows);

    void deleteRows(Map<Integer, String> rows);

    InputStream generateFilteredDocument(String platform, Integer unit, Integer account, String date, Double amount);

}
