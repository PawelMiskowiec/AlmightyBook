package com.example.AlmightyBook.uploads.domain;

import com.example.AlmightyBook.jpa.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Upload extends BaseEntity {
    private byte[] file;

    private String contentType;

    private String fileName;

    @CreatedDate
    private LocalDateTime createdAt;

    public Upload(String fileName, String contentType, byte[] file) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.file = file;
    }
}
