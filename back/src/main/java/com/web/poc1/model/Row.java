package com.web.poc1.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="row")
public class Row {

    @Id
    private Long id;
}
