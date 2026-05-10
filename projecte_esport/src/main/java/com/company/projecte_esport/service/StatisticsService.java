package com.company.projecte_esport.service;

import java.util.Map;

public interface StatisticsService {
    
    // Para usuario individual
    Map<String, Object> getPlayerMonthlyStats(String userId);
    long getTotalWins(String userId);
    long getTotalLosses(String userId);
    long getTotalMatches(String userId);
    String getFavoriteSport(String userId);
    Map<String, Object> getPlayerRanking(String userId);
    Map<String, Object> getPlayerEvolution(String userId);
    
    // Para admin (globales)
    Map<String, Double> getCourtOccupation();
    Map<String, Long> getMostUsedSports();
    Map<String, Long> getMonthlyBookingEvolution();
    Map<String, Long> getTopActivePlayers();
}