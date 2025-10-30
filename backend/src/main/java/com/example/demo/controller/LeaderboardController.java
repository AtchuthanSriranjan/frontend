package com.example.demo.controller;

import com.example.demo.model.LeaderboardEntry;
import com.example.demo.repository.LeaderboardRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@CrossOrigin(origins = "http://localhost:3000")
public class LeaderboardController {

    private final LeaderboardRepository repository;

    public LeaderboardController(LeaderboardRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public void addEntry(@RequestBody LeaderboardEntry entry) {
        repository.save(entry);
    }

    @GetMapping
    public List<LeaderboardEntry> getTopEntries() {
        List<LeaderboardEntry> results = repository.findTop10();
        return results != null ? results : List.of();
    }

    @GetMapping("/{size}")
    public List<LeaderboardEntry> getTopEntriesBySize(@PathVariable int size) {
        List<LeaderboardEntry> results = repository.findTop10BySize(size);
        return results != null ? results : List.of();
    }
}
