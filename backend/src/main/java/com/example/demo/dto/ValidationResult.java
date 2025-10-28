package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    public boolean valid;

    // 0-based indices of rows/columns that violate "exactly one queen"
    public List<Integer> invalidRows = new ArrayList<>();
    public List<Integer> invalidCols = new ArrayList<>();

    // region IDs that violate "exactly one queen"
    public List<Integer> invalidRegions = new ArrayList<>();

    // pairs of cell coordinates that are adjacent conflicts (no touching rule)
    // each entry is [r, c] for a conflicting queen cell
    public List<int[]> diagonalConflicts = new ArrayList<>();
}
