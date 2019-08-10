package com.web.poc1.to;

import com.web.poc1.model.ExcelRow;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class FindRequestTo {
    private List<ExcelRow> rows;
    private int pages;
}
