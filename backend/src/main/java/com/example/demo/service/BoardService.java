package com.example.demo.service;

import com.example.demo.model.Board;
import org.springframework.stereotype.Service;
import java.util.Map;

// Service to provide board data
@Service
public class BoardService {

    // Get the fixed predefined board
    public Map<String, Object> getFixedBoard() {
        Board board = Board.fixedBoard();
        return Map.of(
            "size", board.getSize(),
            "regions", board.getRegions()
        );
    }

    // Get a random board
    public Map<String, Object> getRandomBoard() {
        Board board = Board.randomBoard(7, 7);
        return Map.of(
            "size", board.getSize(),
            "regions", board.getRegions()
        );
    }
}
