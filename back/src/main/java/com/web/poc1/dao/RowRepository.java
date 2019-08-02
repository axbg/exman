package com.web.poc1.dao;

import com.web.poc1.model.ExcelRow;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RowRepository extends CrudRepository<ExcelRow, Long> {
    void deleteByIdIn(List<Long> ids);
}
