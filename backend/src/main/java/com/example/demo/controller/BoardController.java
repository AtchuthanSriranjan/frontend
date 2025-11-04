package com.example.demo.controller;

import com.example.demo.model.Board;
import com.example.demo.service.BoardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "https://queens-project.vercel.app"})
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /** Deterministic board endpoint */
    @GetMapping("/board")
    public Board getFixedBoard(@RequestParam(defaultValue = "7") int size) {
        return boardService.getFixedBoard(size);
    }

    /** Random connected board endpoint */
    @GetMapping("/random-board")
    public Board getRandomBoard(@RequestParam(defaultValue = "7") int size) {
        return boardService.generateRandomBoard(size);
    }
}
