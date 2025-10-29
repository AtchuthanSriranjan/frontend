package com.example.demo.validator;

import com.example.demo.dto.ValidationResult;
import com.example.demo.model.Board;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoardValidator {

    public ValidationResult validate(Board board, String[][] state) {
        int size = board.getSize();
        int[][] regions = board.getRegions();

        List<Integer> invalidRows = new ArrayList<>();
        List<Integer> invalidCols = new ArrayList<>();
        List<Integer> invalidRegions = new ArrayList<>();
        List<int[]> diagonalConflicts = new ArrayList<>();

        // === Rule 1: Only one queen per row ===
        for (int i = 0; i < size; i++) {
            int queens = 0;
            for (int j = 0; j < size; j++) {
                if ("queen".equals(state[i][j])) queens++;
            }
            if (queens > 1) invalidRows.add(i);
        }

        // === Rule 2: Only one queen per column ===
        for (int j = 0; j < size; j++) {
            int queens = 0;
            for (int i = 0; i < size; i++) {
                if ("queen".equals(state[i][j])) queens++;
            }
            if (queens > 1) invalidCols.add(j);
        }

        // === Rule 3: Only one queen per region ===
        Set<Integer> regionSet = new HashSet<>();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if ("queen".equals(state[r][c])) {
                    int region = regions[r][c];
                    if (!regionSet.add(region)) {
                        invalidRegions.add(region);
                    }
                }
            }
        }

        // === Rule 4: No diagonal conflicts ===
        int[] dr = {-1, -1, 1, 1};
        int[] dc = {-1, 1, -1, 1};
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if ("queen".equals(state[r][c])) {
                    for (int d = 0; d < 4; d++) {
                        int nr = r + dr[d];
                        int nc = c + dc[d];
                        if (nr >= 0 && nr < size && nc >= 0 && nc < size && "queen".equals(state[nr][nc])) {
                            diagonalConflicts.add(new int[]{r, c});
                        }
                    }
                }
            }
        }

        boolean isValid = invalidRows.isEmpty()
                && invalidCols.isEmpty()
                && invalidRegions.isEmpty()
                && diagonalConflicts.isEmpty();

        return new ValidationResult(isValid, invalidRows, invalidCols, invalidRegions, diagonalConflicts);
    }
}
