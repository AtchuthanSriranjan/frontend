package com.example.demo.service;

import com.example.demo.dto.BoardRequest;
import com.example.demo.dto.ValidationResult;
import com.example.demo.model.Board;
import com.example.demo.validator.BoardValidator;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    private final BoardValidator boardValidator = new BoardValidator();

    public ValidationResult validateBoard(BoardRequest request) {
        Board board = new Board(request.getSize(), request.getRegions());
        return boardValidator.validate(board, request.getBoard());
    }
}
