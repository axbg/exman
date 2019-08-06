package com.web.poc1.dao;

import com.web.poc1.model.ExcelRow;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface RowRepository extends CrudRepository<ExcelRow, Long>, JpaSpecificationExecutor<ExcelRow> {
    void deleteByIdIn(List<Long> ids);
}
