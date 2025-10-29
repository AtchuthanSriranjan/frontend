package com.example.demo.util;

import java.util.*;

/**
 * Generates connected regions on an N x N grid.
 * Strategy:
 *  - Pick regionCount random seed cells
 *  - Multi-source BFS grows each region in waves (4-neighborhood)
 *  - Enforce per-region capacity caps to keep sizes balanced
 *  - Post-pass smoothing to fix tiny artifacts on borders
 */
public class RegionGenerator {
    private static final Random RAND = new Random();

    public static int[][] generateRegions(int size, int regionCount) {
        int total = size * size;

        // --- capacities: near-equal sizes ---
        int base = total / regionCount;
        int rem  = total % regionCount;
        int[] cap = new int[regionCount];
        for (int i = 0; i < regionCount; i++) {
            cap[i] = base + (i < rem ? 1 : 0);
        }

        int[][] regions = new int[size][size];
        for (int[] row : regions) Arrays.fill(row, -1);

        // --- choose distinct random seeds ---
        List<int[]> seeds = pickDistinctSeeds(size, regionCount);

        // assign seeds
        int[] count = new int[regionCount];
        @SuppressWarnings("unchecked")
        ArrayDeque<int[]>[] queues = new ArrayDeque[regionCount];
        for (int i = 0; i < regionCount; i++) {
            queues[i] = new ArrayDeque<>();
            int r = seeds.get(i)[0], c = seeds.get(i)[1];
            regions[r][c] = i;
            count[i] = 1;
            queues[i].add(new int[]{r, c});
        }

        // --- multi-source BFS with per-region capacity ---
        int[][] DIRS = {{1,0},{-1,0},{0,1},{0,-1}};
        boolean changed;
        do {
            changed = false;
            for (int i = 0; i < regionCount; i++) {
                if (count[i] >= cap[i]) continue; // region i is full
                int waveSize = queues[i].size();
                // expand one "layer" to keep growth fair among regions
                for (int s = 0; s < waveSize && count[i] < cap[i]; s++) {
                    int[] cell = queues[i].poll();
                    if (cell == null) break;
                    int r = cell[0], c = cell[1];
                    for (int[] d : DIRS) {
                        int nr = r + d[0], nc = c + d[1];
                        if (nr < 0 || nc < 0 || nr >= size || nc >= size) continue;
                        if (regions[nr][nc] == -1 && count[i] < cap[i]) {
                            regions[nr][nc] = i;
                            count[i]++;
                            queues[i].add(new int[]{nr, nc});
                            changed = true;
                        }
                    }
                }
            }
        } while (changed);

        // --- safety: any leftover unassigned cells -> attach to nearest assigned neighbor ---
        // (rare with BFS + capacities, but keep for robustness)
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (regions[r][c] == -1) {
                    regions[r][c] = nearestAssignedRegion(regions, r, c);
                }
            }
        }

        // --- smoothing pass: convert “singleton” cells to majority of neighbors ---
        smoothSingletons(regions);

        // --- guarantee that every region id appears at least once ---
        Set<Integer> present = new HashSet<>();
        for (int[] row : regions) {
            for (int v : row) present.add(v);
        }

        for (int id = 0; id < regionCount; id++) {
            if (!present.contains(id)) {
                // Find random cell to reassign to this region
                int tries = 0;
                while (tries < 1000) {
                    int r = RAND.nextInt(size);
                    int c = RAND.nextInt(size);
                    int old = regions[r][c];
                    // Replace only if old region has more than one cell
                    int countOld = 0;
                    for (int[] row : regions) {
                        for (int v : row) if (v == old) countOld++;
                    }
                    if (countOld > 1) {
                        regions[r][c] = id;
                        break;
                    }
                    tries++;
                }
            }
        }


        return regions;
    }

    private static List<int[]> pickDistinctSeeds(int size, int k) {
        Set<Integer> used = new HashSet<>();
        List<int[]> seeds = new ArrayList<>();
        while (seeds.size() < k) {
            int r = RAND.nextInt(size);
            int c = RAND.nextInt(size);
            int key = r * size + c;
            if (used.add(key)) seeds.add(new int[]{r, c});
        }
        return seeds;
    }

    private static int nearestAssignedRegion(int[][] regions, int sr, int sc) {
        int n = regions.length;
        if (regions[sr][sc] != -1) return regions[sr][sc];
        ArrayDeque<int[]> q = new ArrayDeque<>();
        boolean[][] vis = new boolean[n][n];
        q.add(new int[]{sr, sc});
        vis[sr][sc] = true;
        int[][] DIRS = {{1,0},{-1,0},{0,1},{0,-1}};
        while (!q.isEmpty()) {
            int[] cell = q.poll();
            int r = cell[0], c = cell[1];
            if (regions[r][c] != -1) return regions[r][c];
            for (int[] d : DIRS) {
                int nr = r + d[0], nc = c + d[1];
                if (nr < 0 || nc < 0 || nr >= n || nc >= n) continue;
                if (!vis[nr][nc]) {
                    vis[nr][nc] = true;
                    q.add(new int[]{nr, nc});
                }
            }
        }
        // fallback (should not happen)
        return 0;
    }

    private static void smoothSingletons(int[][] regions) {
        int n = regions.length;
        int[][] DIRS = {{1,0},{-1,0},{0,1},{0,-1}};
        boolean changed;
        int passes = 0;
        do {
            changed = false;
            passes++;
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    int id = regions[r][c];
                    // count neighbors by region id
                    Map<Integer, Integer> freq = new HashMap<>();
                    for (int[] d : DIRS) {
                        int nr = r + d[0], nc = c + d[1];
                        if (nr < 0 || nc < 0 || nr >= n || nc >= n) continue;
                        int nid = regions[nr][nc];
                        freq.put(nid, freq.getOrDefault(nid, 0) + 1);
                    }
                    int majorityId = id;
                    int best = -1;
                    for (Map.Entry<Integer, Integer> e : freq.entrySet()) {
                        if (e.getValue() > best) {
                            best = e.getValue();
                            majorityId = e.getKey();
                        }
                    }
                    // if current id is unique (no same-id neighbors) and there is a clear majority neighbor, flip it
                    int sameNeighbors = 0;
                    for (int[] d : DIRS) {
                        int nr = r + d[0], nc = c + d[1];
                        if (nr < 0 || nc < 0 || nr >= n || nc >= n) continue;
                        if (regions[nr][nc] == id) sameNeighbors++;
                    }
                    if (sameNeighbors == 0 && best > 0) { // best>0 means at least one neighbor exists
                        regions[r][c] = majorityId;
                        changed = true;
                    }
                }
            }
            // small cap on smoothing iterations to avoid over-smoothing
        } while (changed && passes < 3);
    }
}
