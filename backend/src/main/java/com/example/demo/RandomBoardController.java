package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

/**
 * Controller to generate and return a random board configuration.
 */
@RestController
public class RandomBoardController {
/**
     * Endpoint to get a random board configuration.
     * The board is a 7x7 grid with each cell assigned a random region ID from 0 to 6.
     *
     * @return A map containing the board size and the regions array.
     */
    @GetMapping("/api/random-board")
    public Map<String, Object> getRandomBoard() {
        int size = 7;
        int regionCount = 7;
        Random rand = new Random();

        int[][] regions = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                regions[i][j] = rand.nextInt(regionCount); // random region ID 0–6
            }
        }

        return Map.of(
                "size", size,
                "regions", regions
        );
    }
}
