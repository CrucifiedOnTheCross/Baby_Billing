package com.nexign.cdr.controller;

import com.nexign.cdr.service.CallGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calls")
public class CallController {

    private final CallGenerationService callGenerationService;

    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestParam int count) throws InterruptedException {
        callGenerationService.generateAndSaveCalls(count);
        return ResponseEntity.ok("Звонки сгенерированы и сохранены.");
    }

}
