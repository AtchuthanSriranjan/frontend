package com.example.demo.controller;

import com.example.demo.dto.BoardRequest;
import com.example.demo.dto.ValidationResult;
import com.example.demo.service.ValidationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ValidationController {

    private final ValidationService validationService;

    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @PostMapping("/validate")
    public ValidationResult validate(@RequestBody BoardRequest request) {
        return validationService.validate(request.getBoard(),
            request.getRegions(), request.getSize());
    }
}
