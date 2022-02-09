package com.example.AlmightyBook.uploads.web;

import com.example.AlmightyBook.catalog.application.port.CatalogUseCase;
import com.example.AlmightyBook.uploads.application.port.UploadUseCase;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/uploads")
@AllArgsConstructor
public class UploadsController {
    private final UploadUseCase upload;
    private final CatalogUseCase catalog;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UploadResponse> getUpload(@PathVariable Long id) {
        return upload.getById(id)
                .map(file -> {
                    UploadResponse response = new UploadResponse(
                            file.getId(),
                            file.getContentType(),
                            file.getFileName(),
                            file.getCreatedAt()
                    );
                    return ResponseEntity.ok(response);
                }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/file")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Resource> getUploadFile(@PathVariable Long id) {
        return upload.getById(id)
                .map(file -> {
                    String contentDisposition = "attachment; filename=\"" + file.getFileName() + "\"";
                    Resource resource = new ByteArrayResource(file.getFile());
                    return ResponseEntity
                            .ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                            .contentType(MediaType.parseMediaType(file.getContentType()))
                            .body(resource);

                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/cover")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBookCover(@PathVariable Long id) {
        catalog.removeBookCover(id);
    }

    @Value
    @AllArgsConstructor
    static class UploadResponse {
        Long id;
        String contentType;
        String fileName;
        LocalDateTime createdAt;
    }
}
