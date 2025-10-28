package com.example.demo.dto;

public class BoardRequest {
    public int size;
    public int[][] regions;      // same matrix you serve at /api/board
    public String[][] board;     // "empty" | "x" | "queen"
}
