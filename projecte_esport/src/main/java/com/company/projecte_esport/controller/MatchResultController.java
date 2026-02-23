package com.company.projecte_esport.controller;

import com.company.projecte_esport.dto.MatchResultDTO;
import com.company.projecte_esport.service.MatchResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "*")
public class MatchResultController {

    @Autowired
    private MatchResultService matchResultService;

    @PostMapping
    public ResponseEntity<MatchResultDTO> createResult(@RequestBody MatchResultDTO dto) {
        return ResponseEntity.ok(matchResultService.createMatchResult(dto));
    }

    @GetMapping("/wins/{playerId}")
    public ResponseEntity<List<MatchResultDTO>> getPlayerWins(@PathVariable String playerId) {
        return ResponseEntity.ok(matchResultService.getWinsByPlayerId(playerId));
    }
}