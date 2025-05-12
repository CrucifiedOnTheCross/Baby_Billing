package com.nexign.brt.controller;

import com.nexign.brt.dto.CdrDto;
import com.nexign.brt.service.CdrProcessingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/cdr")
@RequiredArgsConstructor
public class CdrController {

    private final CdrProcessingService callService;

    @PostMapping("/process")
    public ResponseEntity<String> processCdrRecords(@Valid @RequestBody List<CdrDto> cdrList) {
        callService.process(cdrList);
        return ResponseEntity.ok("CDR records processed successfully.");
    }

}
