package com.example.demo.controller;

import com.example.demo.service.BoardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/api/board")
    public Map<String, Object> getBoard() {
        return boardService.getFixedBoard();
    }
}
