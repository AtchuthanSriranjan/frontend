package com.example.demo.controller;

import com.example.demo.service.BoardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class RandomBoardController {

    private final BoardService boardService;

    public RandomBoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/api/random-board")
    public Map<String, Object> getRandomBoard() {
        return boardService.getRandomBoard();
    }
}
