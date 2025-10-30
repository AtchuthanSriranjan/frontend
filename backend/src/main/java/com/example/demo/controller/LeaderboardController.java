package com.example.demo.controller;

import com.example.demo.model.LeaderboardEntry;
import com.example.demo.repository.LeaderboardRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@CrossOrigin(origins = "http://localhost:3000") // allow your React frontend
public class LeaderboardController {

    // Repository for leaderboard entries
    private final LeaderboardRepository repository;

    // Constructor injection of the repository
    public LeaderboardController(LeaderboardRepository repository) {
        this.repository = repository;
    }

    // Add a new leaderboard entry
    @PostMapping
    public void addEntry(@RequestBody LeaderboardEntry entry) {
        repository.save(entry);
    }

    // Get top 10 leaderboard entries
    @GetMapping
    public List<LeaderboardEntry> getTopEntries() {
        return repository.findTop10();
    }
}
