package com.example.demo.util;

import java.util.Random;

public class RegionGenerator {

    /** Generates a region map with exactly `size` distinct region IDs (0..size-1). */
    public static int[][] generateConnectedRegions(int size) {
        if (size <= 0) return new int[0][0];

        int[][] regions = new int[size][size];
        Random rand = new Random();

        // Simple filler: allow full range 0..size-1
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                regions[i][j] = rand.nextInt(size);
            }
        }

        // Ensure presence of every id 0..size-1 at least once
        // Minimal guarantee: stamp the first row to contain all ids
        for (int id = 0; id < size && id < size; id++) {
            regions[0][id] = id;
        }

        return regions;
    }
}
