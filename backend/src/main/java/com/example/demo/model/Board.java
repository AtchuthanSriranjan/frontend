package com.example.demo.model;

import java.util.Random;

public class Board {
    private int size;
    private int[][] regions;

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

    // Factory method to create a random board
    public static Board randomBoard(int size, int regionCount) {
        int[][] regions = com.example.demo.util.RegionGenerator.generateRegions(size, regionCount);
        return new Board(size, regions);
    }


    // Factory method to create the fixed predefined board
    public static Board fixedBoard() {
        int[][] regions = {
            {0,0,0,0,0,0,0},
            {0,0,1,0,2,2,0},
            {0,1,1,3,3,2,2},
            {0,1,4,4,3,3,2},
            {0,0,0,4,4,2,2},
            {0,0,6,6,5,5,2},
            {0,0,0,6,6,5,5}
        };
        return new Board(7, regions);
    }
}
