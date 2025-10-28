package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class BoardController {

    @GetMapping("/api/board")
    public Map<String, Object> getBoard() {
        int[][] regions = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 2, 2, 0},
                {0, 1, 1, 3, 3, 2, 2},
                {0, 1, 4, 4, 3, 3, 2},
                {0, 0, 0, 4, 4, 2, 2},
                {0, 0, 6, 6, 5, 5, 2},
                {0, 0, 0, 6, 6, 5, 5}
        };

        return Map.of(
                "size", 7,
                "regions", regions
        );
    }
}
