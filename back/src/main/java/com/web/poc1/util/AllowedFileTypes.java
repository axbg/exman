package com.web.poc1.util;

import lombok.Getter;

@Getter
public enum AllowedFileTypes {
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    XLS("application/vnd.ms-excel");

    private final String type;

    AllowedFileTypes(final String type) {
        this.type = type;
    }
}
