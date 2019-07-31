package com.web.poc1.service;

import com.web.poc1.dao.RowRepository;
import com.web.poc1.exception.CustomException;
import com.web.poc1.util.AllowedFileTypes;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Iterator;

@Service
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    RowRepository rowRepository;

    @Override
    public int uploadDocument(MultipartFile document) throws CustomException {

        if (document.getContentType().equals(AllowedFileTypes.XLSX.getType())) {
            return storeXLSX(document);
        } else if(document.getContentType().equals(AllowedFileTypes.XLS.getType())){
            System.out.println("bad");
        } else {
            throw new CustomException("File type not supported", HttpStatus.BAD_REQUEST);
        }

        return 1;
    }

    private int storeXLSX(MultipartFile document) {
        int numberOfRows = 0;

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(document.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                System.out.println(row.getCell(0).toString());
                System.out.println(row.getCell(1).toString());
                System.out.println(row.getCell(2).toString());
                System.out.println(row.getCell(3).toString());
                System.out.println(row.getCell(4).toString());
            }

        } catch (IOException ex) {
            throw new CustomException("Excel file is corrupted", HttpStatus.BAD_REQUEST);
        }

        return 1;
    }

}
