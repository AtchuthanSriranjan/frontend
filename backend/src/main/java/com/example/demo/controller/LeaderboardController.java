package com.example.demo.controller;

import com.example.demo.model.LeaderboardEntry;
import com.example.demo.repository.LeaderboardRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@CrossOrigin(origins = {"http://localhost:3000", "https://queens-project.vercel.app"})
public class LeaderboardController {

    private final LeaderboardRepository repository;

    public LeaderboardController(LeaderboardRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public LeaderboardEntry addEntry(@RequestBody LeaderboardEntry entry) {
        // Normalize basic fields
        if (entry.getName() == null || entry.getName().isBlank()) {
            entry.setName("Guest");
        }
        if (entry.getBoardType() == null || entry.getBoardType().isBlank()) {
            entry.setBoardType("fixed");
        }
        return repository.saveAndReturn(entry);
    }

    @GetMapping
    public List<LeaderboardEntry> getTopEntries() {
        List<LeaderboardEntry> results = repository.findTop10();
        return results != null ? results : List.of();
    }

    @GetMapping("/{size}")
    public List<LeaderboardEntry> getLeaderboard(@PathVariable Integer size) {
        List<LeaderboardEntry> results = repository.findBySizeOrderByTimeSecondsAsc(size);
        return results != null ? results : List.of();
    }

}
