package com.example.AlmightyBook.uploads.db;

import com.example.AlmightyBook.uploads.domain.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadJpaRepository extends JpaRepository<Upload, Long> {
}
