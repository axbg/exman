package com.web.poc1.service;

import com.web.poc1.dao.RowRepository;
import com.web.poc1.exception.CustomException;
import com.web.poc1.model.ExcelRow;
import com.web.poc1.util.AllowedFileTypes;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    RowRepository rowRepository;

    @Override
    public int uploadDocument(MultipartFile document) throws CustomException {
        if (document.getContentType().equals(AllowedFileTypes.XLSX.getType())) {
            return storeXLSX(document);
        } else if (document.getContentType().equals(AllowedFileTypes.XLS.getType())) {
            return storeXLS(document);
        } else {
            throw new CustomException("File type not supported", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<ExcelRow> getRows() {
        return (List<ExcelRow>) rowRepository.findAll();
    }

    @Override
    public ExcelRow updateRow(ExcelRow row) throws CustomException {
        row.hasAnyNull();
        return rowRepository.save(row);
    }

    @Override
    public void deleteRow(Long id) {
        try {
            rowRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {

        }
    }

    private int storeXLSX(MultipartFile document) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(document.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            return parseSheet(sheet);
        } catch (IOException ex) {
            throw new CustomException("Excel file is corrupted", HttpStatus.BAD_REQUEST);
        }
    }

    private int storeXLS(MultipartFile document) {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(document.getInputStream());
            HSSFSheet sheet = workbook.getSheetAt(0);
            return parseSheet(sheet);
        } catch (IOException ex) {
            throw new CustomException("Excel file is corrupted", HttpStatus.BAD_REQUEST);
        }
    }

    private int parseSheet(Sheet sheet) {
        List<ExcelRow> rows = new ArrayList<>();

        if (sheet.getRow(0).getCell(0).getStringCellValue().equals("Platform")) {
            sheet.removeRow(sheet.getRow(0));
        }

        for (Row row : sheet) {
            rows.add(createExcelRow(row));
        }

        rowRepository.saveAll(rows);
        return sheet.getPhysicalNumberOfRows();
    }

    private ExcelRow createExcelRow(Row row) throws CustomException {
        nullCheck(row);

        String platform = row.getCell(0).getStringCellValue();
        Integer unit = (int) row.getCell(1).getNumericCellValue();
        Integer account = (int) row.getCell(2).getNumericCellValue();
        Double amount = row.getCell(4).getNumericCellValue();

        Date date;
        java.util.Date parsedDate = row.getCell(3).getDateCellValue();
        date = new Date(parsedDate.getTime());

        return new ExcelRow(platform, unit, account, date, amount);
    }

    private void nullCheck(Row row) throws CustomException {
        for (int index = 0; index < 5; index++) {
            if (row.getCell(index) == null) {
                throw new CustomException("Empty cell: row " + row.getRowNum() + ", cell " + (index + 1), HttpStatus.BAD_REQUEST);
            }
        }
    }

}
