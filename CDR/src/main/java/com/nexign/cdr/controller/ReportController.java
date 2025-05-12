package com.nexign.cdr.controller;

import com.nexign.cdr.service.CsvExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final CsvExportService csvExportService;

    @GetMapping("/latest")
    public ResponseEntity<Resource> downloadLatestCsv() {
        File latestFile = csvExportService.getLatestCsvFile();
        if (latestFile == null || !latestFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(latestFile);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + latestFile.getName() + "\"")
                .contentLength(latestFile.length())
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }
}
