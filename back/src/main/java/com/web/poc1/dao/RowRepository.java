package com.web.poc1.dao;

import com.web.poc1.model.ExcelRow;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RowRepository extends CrudRepository<ExcelRow, Long> {

}
