package com.web.poc1.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "excel_row")
@AllArgsConstructor
@Getter
@Setter
public class ExcelRow {

    @Id
    private Long id;

    private String platform;

    private Integer unit;

    private Integer account;

    private Date date;

    private Float amount;
}
