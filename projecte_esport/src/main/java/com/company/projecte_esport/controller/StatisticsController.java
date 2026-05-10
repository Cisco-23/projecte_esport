package com.company.projecte_esport.controller;

import com.company.projecte_esport.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "*")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    // ESTADÍSTICAS DE USUARIO
    
    @GetMapping("/player/{userId}/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlyStats(@PathVariable String userId) {
        return ResponseEntity.ok(statisticsService.getPlayerMonthlyStats(userId));
    }

    @GetMapping("/player/{userId}/wins")
    public ResponseEntity<Long> getTotalWins(@PathVariable String userId) {
        return ResponseEntity.ok(statisticsService.getTotalWins(userId));
    }

    @GetMapping("/player/{userId}/losses")
    public ResponseEntity<Long> getTotalLosses(@PathVariable String userId) {
        return ResponseEntity.ok(statisticsService.getTotalLosses(userId));
    }

    @GetMapping("/player/{userId}/matches")
    public ResponseEntity<Long> getTotalMatches(@PathVariable String userId) {
        return ResponseEntity.ok(statisticsService.getTotalMatches(userId));
    }

    @GetMapping("/player/{userId}/favorite")
    public ResponseEntity<String> getFavoriteSport(@PathVariable String userId) {
        return ResponseEntity.ok(statisticsService.getFavoriteSport(userId));
    }

    @GetMapping("/player/{userId}/ranking")
    public ResponseEntity<Map<String, Object>> getPlayerRanking(@PathVariable String userId) {
        return ResponseEntity.ok(statisticsService.getPlayerRanking(userId));
    }

    @GetMapping("/player/{userId}/evolution")
    public ResponseEntity<Map<String, Object>> getPlayerEvolution(@PathVariable String userId) {
        return ResponseEntity.ok(statisticsService.getPlayerEvolution(userId));
    }

    // ESTADÍSTICAS GLOBALES (ADMIN)
    
    @GetMapping("/global/occupation")
    public ResponseEntity<Map<String, Double>> getCourtOccupation() {
        return ResponseEntity.ok(statisticsService.getCourtOccupation());
    }

    @GetMapping("/global/sports")
    public ResponseEntity<Map<String, Long>> getMostUsedSports() {
        return ResponseEntity.ok(statisticsService.getMostUsedSports());
    }

    @GetMapping("/global/monthly")
    public ResponseEntity<Map<String, Long>> getMonthlyBookingEvolution() {
        return ResponseEntity.ok(statisticsService.getMonthlyBookingEvolution());
    }

    @GetMapping("/global/top-players")
    public ResponseEntity<Map<String, Long>> getTopActivePlayers() {
        return ResponseEntity.ok(statisticsService.getTopActivePlayers());
    }
}