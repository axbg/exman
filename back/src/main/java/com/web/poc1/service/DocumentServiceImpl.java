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
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public ExcelRow createRow() throws CustomException {
        ExcelRow newRow = new ExcelRow("new", 0, 0, new Date(System.currentTimeMillis()), 0.0);
        return rowRepository.save(newRow);
    }

    @Override
    public List<ExcelRow> updateRows(String rows) {
        List<ExcelRow> excelRows = parseJsonArray(rows);
        this.rowRepository.saveAll(excelRows);
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public void deleteRows(Map<Integer, String> rows) {
        try {

            List<Long> ids = new ArrayList();
            for (Map.Entry<Integer, String> e : rows.entrySet()) {
                ids.add(Long.parseLong(e.getValue()));
            }

            rowRepository.deleteByIdIn(ids);
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

    private List<ExcelRow> parseJsonArray(String json) {
        List<ExcelRow> result = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int index = 0; index < jsonArray.length(); index++) {
                result.add(parseJson(jsonArray.getJSONObject(index)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private ExcelRow parseJson(JSONObject json) {
        ExcelRow excelRow = new ExcelRow();
        try {
            excelRow.setId(json.getLong("id"));
            excelRow.setPlatform(json.getString("platform"));
            excelRow.setUnit(json.getInt("unit"));
            excelRow.setAccount(json.getInt("account"));
            excelRow.setAmount(json.getDouble("amount"));

            Date date;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            java.util.Date parsedDate = simpleDateFormat.parse(json.getString("date"));
            date = new Date(parsedDate.getTime());

            excelRow.setDate(date);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return excelRow;
    }

    @Override
    public List<ExcelRow> findByDynamicSelector(String platform, Pageable pageable) {
        Page page = rowRepository.findAll(new Specification<ExcelRow>() {
            @Override
            public Predicate toPredicate(Root<ExcelRow> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (platform != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("platform"), platform)));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);

        page.getTotalElements();
        page.getTotalPages();

        return page.getContent();
    }

}
