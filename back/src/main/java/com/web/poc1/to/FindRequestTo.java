package com.web.poc1.to;

import com.web.poc1.model.ExcelRow;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class FindRequestTo {
    @NonNull
    private List<ExcelRow> rows;

    @NonNull
    private long pages;
    private List<String> platforms;
    private List<Date> dates;
}
