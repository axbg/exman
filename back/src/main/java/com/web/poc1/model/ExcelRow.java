package com.web.poc1.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.web.poc1.service.DateSerializer;
import lombok.*;

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

}
