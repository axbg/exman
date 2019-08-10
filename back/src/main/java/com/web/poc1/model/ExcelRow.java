package com.web.poc1.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.web.poc1.exception.CustomException;
import com.web.poc1.service.DateSerializer;
import lombok.*;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "excel_row")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class ExcelRow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String platform;

    @NonNull
    private Integer unit;

    @NonNull
    private Integer account;

    @NonNull
    @JsonSerialize(using = DateSerializer.class)
    private Date date;

    @NonNull
    private Double amount;

    public void hasAnyNull() throws CustomException {
        String nullField = null;

        if (id == null) {
            nullField = "id";
        } else if (platform == null) {
            nullField = "platform";
        } else if (unit == null) {
            nullField = "unit";
        } else if (account == null) {
            nullField = "account";
        } else if (date == null) {
            nullField = "date";
        } else if (amount == null) {
            nullField = "amount";
        }

        if (nullField != null) {
            throw new CustomException("Field " + nullField + " is empty", HttpStatus.BAD_REQUEST);
        }
    }
}
