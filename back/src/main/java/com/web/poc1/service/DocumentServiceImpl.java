package com.web.poc1.service;

import com.web.poc1.dao.RowRepository;
import com.web.poc1.exception.CustomException;
import com.web.poc1.model.ExcelRow;
import com.web.poc1.to.FindRequestTo;
import com.web.poc1.util.AllowedFileTypes;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public FindRequestTo findByDynamicSelector(String platform, boolean meta, Integer unit, Integer account, String date, Double amount, Pageable pageable)
            throws CustomException {
        Page page = rowRepository.findAll((Specification<ExcelRow>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            addCriterias(platform, unit, account, date, amount, root, predicates, criteriaBuilder);
            query.orderBy(criteriaBuilder.desc(root.get("id")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }, pageable);

        return !meta ? new FindRequestTo(page.getContent(), page.getTotalElements())
                : this.enrichWithMeta(new FindRequestTo(page.getContent(), page.getTotalElements()));
    }

    @Override
    public ExcelRow createRow() throws CustomException {
        ExcelRow newRow = new ExcelRow("new", 0, 0, new Date(System.currentTimeMillis()), 0.0);
        return rowRepository.save(newRow);
    }

    @Override
    public FindRequestTo updateRows(String rows) {
        List<ExcelRow> excelRows = parseJsonArray(rows);
        this.rowRepository.saveAll(excelRows);
        return enrichWithMeta(new FindRequestTo(new ArrayList<>(), 0));
    }

    @Override
    @Transactional
    public void deleteRows(Map<Integer, String> rows) {
        try {

            List<Long> ids = new ArrayList<>();
            for (Map.Entry<Integer, String> e : rows.entrySet()) {
                ids.add(Long.parseLong(e.getValue()));
            }

            rowRepository.deleteByIdIn(ids);
        } catch (EmptyResultDataAccessException exception) {
            //continue execution
        }
    }

    @Override
    public InputStream generateFilteredDocument(String platform, Integer unit, Integer account, String date, Double amount) throws CustomException {

        List<ExcelRow> results = rowRepository.findAll((Specification<ExcelRow>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            addCriterias(platform, unit, account, date, amount, root, predicates, criteriaBuilder);
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        });

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("result");

        Row headerRow = sheet.createRow(0);
        Cell platformCell = headerRow.createCell(0);
        platformCell.setCellValue("Platform");

        Cell unitCell = headerRow.createCell(1);
        unitCell.setCellValue("Unit");

        Cell accountCell = headerRow.createCell(2);
        accountCell.setCellValue("Account");

        Cell dateCell = headerRow.createCell(3);
        dateCell.setCellValue("Date");

        Cell amountCell = headerRow.createCell(4);
        amountCell.setCellValue("Amount");

        for (int index = 1, cellIndex = 0; index <= results.size(); index++, cellIndex = 0) {
            Row row = sheet.createRow(index);

            Cell platformDataCell = row.createCell(cellIndex++);
            platformDataCell.setCellValue(results.get(index - 1).getPlatform());

            Cell unitDataCell = row.createCell(cellIndex++);
            unitDataCell.setCellValue(results.get(index - 1).getUnit());

            Cell accountDataCell = row.createCell(cellIndex++);
            accountDataCell.setCellValue(results.get(index - 1).getAccount());

            CellStyle dateFormat = workbook.createCellStyle();
            CreationHelper styleHelper = workbook.getCreationHelper();
            dateFormat.setDataFormat(styleHelper.createDataFormat().getFormat("yyyy/MM/dd"));

            Cell dateDataCell = row.createCell(cellIndex++);
            dateDataCell.setCellValue(results.get(index - 1).getDate());
            dateDataCell.setCellStyle(dateFormat);

            Cell amountDataCell = row.createCell(cellIndex);
            amountDataCell.setCellValue(results.get(index - 1).getAmount());
        }

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            workbook.write(output);
            return new ByteArrayInputStream(output.toByteArray());
        } catch (IOException e) {
            throw new CustomException("Error generating document", HttpStatus.INTERNAL_SERVER_ERROR);
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

    private void addCriterias(String platform, Integer unit, Integer account, String date, Double amount,
                              Root<ExcelRow> root, List<Predicate> predicates, CriteriaBuilder criteriaBuilder) throws CustomException {
        if (platform != null) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("platform"), platform)));
        }

        if (unit != null) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("unit"), unit)));
        }

        if (account != null) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("account"), account)));
        }

        if (date != null) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                java.util.Date parsedDate = simpleDateFormat.parse(date);
                Date trueDate = new Date(parsedDate.getTime());
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("date"), trueDate)));
            } catch (ParseException e) {
                throw new CustomException("Not a valid date", HttpStatus.BAD_REQUEST);
            }
        }

        if (amount != null) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("amount"), amount)));
        }
    }

    private FindRequestTo enrichWithMeta(FindRequestTo findRequestTo) {
        findRequestTo.setPlatforms(this.rowRepository.getDistinctPlatforms());
        findRequestTo.setDates(this.rowRepository.getDistinctDates());
        return findRequestTo;
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
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        return excelRow;
    }
}
