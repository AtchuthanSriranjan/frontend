package com.example.demo.dto;

import java.util.List;

public class ValidationResult {
    private boolean valid;
    private List<Integer> invalidRows;
    private List<Integer> invalidCols;
    private List<Integer> invalidRegions;
    private List<int[]> diagonalConflicts;

    public ValidationResult(boolean valid, List<Integer> invalidRows, List<Integer> invalidCols,
                            List<Integer> invalidRegions, List<int[]> diagonalConflicts) {
        this.valid = valid;
        this.invalidRows = invalidRows;
        this.invalidCols = invalidCols;
        this.invalidRegions = invalidRegions;
        this.diagonalConflicts = diagonalConflicts;
    }

    public boolean isValid() {
        return valid;
    }

    public List<Integer> getInvalidRows() {
        return invalidRows;
    }

    public List<Integer> getInvalidCols() {
        return invalidCols;
    }

    public List<Integer> getInvalidRegions() {
        return invalidRegions;
    }

    public List<int[]> getDiagonalConflicts() {
        return diagonalConflicts;
    }
}
