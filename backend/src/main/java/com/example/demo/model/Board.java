package com.example.demo.model;

import com.example.demo.util.RegionGenerator;

public class Board {

    private final int size;
    private final int[][] regions;

    public Board(int size, int[][] regions) {
        this.size = size;
        this.regions = regions;
    }

    public int getSize() {
        return size;
    }

    public int[][] getRegions() {
        return regions;
    }

    // --- Factory methods ---

    // Deterministic “fixed” layout – same pattern each time for given size
    public static Board fixedBoard(int size) {
        int[][] regions = generateFixedRegions(size);
        return new Board(size, regions);
    }

    // Randomly generated connected regions
    public static Board randomBoard(int size) {
        int[][] regions = RegionGenerator.generateConnectedRegions(size);
        return new Board(size, regions);
    }

    // --- Helpers ---
    //  Simple fixed pattern generator with exactly `size` region IDs (0..size-1)
    private static int[][] generateFixedRegions(int size) {
        int[][] regions = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                regions[i][j] = (i + j) % Math.max(1, size);
            }
        }
        return regions;
    }
}
