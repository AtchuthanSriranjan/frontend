package com.example.demo;

import com.example.demo.dto.BoardRequest;
import com.example.demo.dto.ValidationResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ValidationController {

    @PostMapping("/api/validate")
    public ValidationResult validate(@RequestBody BoardRequest req) {
        ValidationResult result = new ValidationResult();

        int n = req.size;
        String[][] board = req.board;
        int[][] regions = req.regions;

        // Collect queen positions (ignore "x" and "empty")
        List<int[]> queens = new ArrayList<>();
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if ("queen".equals(board[r][c])) {
                    queens.add(new int[]{r, c});
                }
            }
        }

        // Rule 1: exactly one queen per row
        for (int r = 0; r < n; r++) {
            int count = 0;
            for (int c = 0; c < n; c++) {
                if ("queen".equals(board[r][c])) count++;
            }
            if (count != 1) result.invalidRows.add(r);
        }

        // Rule 2: exactly one queen per column
        for (int c = 0; c < n; c++) {
            int count = 0;
            for (int r = 0; r < n; r++) {
                if ("queen".equals(board[r][c])) count++;
            }
            if (count != 1) result.invalidCols.add(c);
        }

        // Rule 3: exactly one queen per color region (by region id)
        Map<Integer, Integer> regionQueenCount = new HashMap<>();
        Set<Integer> allRegionIds = new HashSet<>();
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                int regionId = regions[r][c];
                allRegionIds.add(regionId);
                if ("queen".equals(board[r][c])) {
                    regionQueenCount.put(regionId, regionQueenCount.getOrDefault(regionId, 0) + 1);
                }
            }
        }
        for (int regionId : allRegionIds) {
            int count = regionQueenCount.getOrDefault(regionId, 0);
            if (count != 1) result.invalidRegions.add(regionId);
        }

        // Rule 4: queens cannot "touch" each other (no adjacency in any of 8 directions)
        // We interpret "touch" as adjacent (king's move) conflicts.
        // If you later want "no diagonal lines at any distance", we can change this check.
        boolean[][] isQueen = new boolean[n][n];
        for (int[] q : queens) isQueen[q[0]][q[1]] = true;

        int[] dr = {-1,-1,-1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1,-1, 1,-1, 0, 1};
        Set<String> conflicted = new HashSet<>();

        for (int[] q : queens) {
            int r = q[0], c = q[1];
            for (int k = 0; k < 8; k++) {
                int nr = r + dr[k], nc = c + dc[k];
                if (nr >= 0 && nr < n && nc >= 0 && nc < n && isQueen[nr][nc]) {
                    conflicted.add(r + "," + c);
                    conflicted.add(nr + "," + nc);
                }
            }
        }
        for (String s : conflicted) {
            String[] parts = s.split(",");
            result.diagonalConflicts.add(new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
        }

        // Valid if no violations
        result.valid = result.invalidRows.isEmpty()
                && result.invalidCols.isEmpty()
                && result.invalidRegions.isEmpty()
                && result.diagonalConflicts.isEmpty();

        return result;
    }
}
