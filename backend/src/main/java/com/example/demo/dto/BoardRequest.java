package com.example.demo.dto;

public class BoardRequest {
    private int size;
    private int[][] regions;
    private String[][] board;

    public int getSize() {
        return size;
    }

    public int[][] getRegions() {
        return regions;
    }

    public String[][] getBoard() {
        return board;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setRegions(int[][] regions) {
        this.regions = regions;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }
}
