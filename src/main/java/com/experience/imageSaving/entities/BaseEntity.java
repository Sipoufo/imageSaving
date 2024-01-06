package com.experience.imageSaving.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    protected Long id;

    @CreatedBy
    protected String createdBy;

    @JsonFormat(pattern = "dd/MM/yyyy HH:m:ss", timezone = "GMT+1")
    @CreatedDate
    protected Date createdDate;

    @LastModifiedBy
    protected String updatedBy;

    @JsonFormat(pattern = "dd/MM/yyyy HH:m:ss", timezone = "GMT+1")
    @LastModifiedDate
    protected Date updatedDate;

    @Column
    protected boolean status = true;
}
