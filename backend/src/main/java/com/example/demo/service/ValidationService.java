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
        return validate(request.getBoard(), request.getRegions(), request.getSize());
    }

    // Overload to match controller signature
    public ValidationResult validate(String[][] state, int[][] regions, int size) {
        int n = (regions != null && regions.length > 0) ? regions.length : size;
        if (n <= 0) {
            return new ValidationResult(false,
                    java.util.List.of(), java.util.List.of(), java.util.List.of(), java.util.List.of());
        }

        // Basic shape guards
        if (state == null || state.length != n) {
            return new ValidationResult(false,
                    java.util.List.of(), java.util.List.of(), java.util.List.of(), java.util.List.of());
        }
        for (String[] row : state) {
            if (row == null || row.length != n) {
                return new ValidationResult(false,
                        java.util.List.of(), java.util.List.of(), java.util.List.of(), java.util.List.of());
            }
        }
        if (regions == null || regions.length != n) {
            return new ValidationResult(false,
                    java.util.List.of(), java.util.List.of(), java.util.List.of(), java.util.List.of());
        }
        for (int[] row : regions) {
            if (row == null || row.length != n) {
                return new ValidationResult(false,
                        java.util.List.of(), java.util.List.of(), java.util.List.of(), java.util.List.of());
            }
        }

        Board board = new Board(n, regions);
        return boardValidator.validate(board, state);
    }
}
