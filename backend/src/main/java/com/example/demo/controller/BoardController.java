package com.example.demo.controller;

import com.example.demo.model.Board;
import com.example.demo.service.BoardService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

    /** Random connected board endpoint (no-cache) */
    @GetMapping("/random-board")
    public ResponseEntity<Board> getRandomBoard(@RequestParam(defaultValue = "7") int size) {
        Board board = boardService.generateRandomBoard(size);
        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate, max-age=0")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .body(board);
    }
}
