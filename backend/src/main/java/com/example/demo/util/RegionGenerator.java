package com.example.demo.util;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RegionGenerator {

    /**
     * Generates a partition of an N×N grid into N connected regions.
     * Each region id is in [0..N-1] and forms a 4-connected component.
     */
    public static int[][] generateConnectedRegions(int size) {
        if (size <= 0) return new int[0][0];

        int[][] regions = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) regions[i][j] = -1;
        }

        Random rand = new Random();

        // Pick distinct random seeds for region ids 0..size-1
        Set<Integer> used = new HashSet<>();
        @SuppressWarnings("unchecked")
        ArrayDeque<int[]>[] queues = new ArrayDeque[size];
        for (int i = 0; i < size; i++) queues[i] = new ArrayDeque<>();

        for (int id = 0; id < size; id++) {
            int r, c, key;
            do {
                r = rand.nextInt(size);
                c = rand.nextInt(size);
                key = r * size + c;
            } while (!used.add(key));
            regions[r][c] = id;
            queues[id].add(new int[]{r, c});
        }

        int remaining = size * size - size; // already assigned seeds
        int[][] DIRS = {{1,0},{-1,0},{0,1},{0,-1}};

        // Multi-source BFS expansion in waves to keep growth fair
        while (remaining > 0) {
            boolean progressed = false;
            for (int id = 0; id < size; id++) {
                int wave = queues[id].size();
                for (int s = 0; s < wave && remaining > 0; s++) {
                    int[] cell = queues[id].poll();
                    if (cell == null) break;
                    int r = cell[0], c = cell[1];
                    for (int[] d : DIRS) {
                        int nr = r + d[0], nc = c + d[1];
                        if (nr < 0 || nc < 0 || nr >= size || nc >= size) continue;
                        if (regions[nr][nc] == -1) {
                            regions[nr][nc] = id;
                            queues[id].add(new int[]{nr, nc});
                            remaining--;
                            progressed = true;
                            if (remaining == 0) break;
                        }
                    }
                }
                if (remaining == 0) break;
            }

            // Safety: if no region could expand (pathologically should not happen),
            // assign one remaining unfilled cell to a random region to unblock.
            if (!progressed && remaining > 0) {
                for (int r = 0; r < size && remaining > 0; r++) {
                    for (int c = 0; c < size && remaining > 0; c++) {
                        if (regions[r][c] == -1) {
                            int id = rand.nextInt(size);
                            regions[r][c] = id;
                            queues[id].add(new int[]{r, c});
                            remaining--;
                        }
                    }
                }
            }
        }

        return regions;
    }
}
