package com.example.demo.service;

import com.example.demo.model.Board;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    /** Returns deterministic fixed layout */
    public Board getFixedBoard(int size) {
        return Board.fixedBoard(size);
    }

    /** Returns random connected layout */
    public Board generateRandomBoard(int size) {
        return Board.randomBoard(size);
    }
}
