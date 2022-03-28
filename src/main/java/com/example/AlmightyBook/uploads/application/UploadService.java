package com.example.AlmightyBook.uploads.application;

import com.example.AlmightyBook.uploads.application.port.UploadUseCase;
import com.example.AlmightyBook.uploads.db.UploadJpaRepository;
import com.example.AlmightyBook.uploads.domain.Upload;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@AllArgsConstructor
public class UploadService implements UploadUseCase {
    private final UploadJpaRepository repository;

    @Override
    public Upload save(SaveUploadCommand command) {

        Upload upload = new Upload(
            command.getFileName(),
            command.getContentType(),
            command.getFile()
        );
        repository.save(upload);
        log.info("Upload saved: " + upload.getFileName());
        return upload;
    }

    @Override
    public Optional<Upload> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void removeById(Long id) {
        repository.deleteById(id);
    }
}
