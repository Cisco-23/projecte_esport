package com.company.projecte_esport.serviceImpl;

import com.company.projecte_esport.model.Booking;
import com.company.projecte_esport.model.MatchResult;
import com.company.projecte_esport.repository.BookingRepository;
import com.company.projecte_esport.repository.MatchResultRepository;
import com.company.projecte_esport.repository.UserRepository;
import com.company.projecte_esport.service.StatisticsService;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final MatchResultRepository matchResultRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public StatisticsServiceImpl(MatchResultRepository matchResultRepository, 
                               BookingRepository bookingRepository,
                               UserRepository userRepository) {
        this.matchResultRepository = matchResultRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Map<String, Object> getPlayerMonthlyStats(String userId) {
        List<Booking> bookings = bookingRepository.findByPlayer1IdOrPlayer2Id(userId, userId);
        
        Map<String, Long> monthlyCount = new TreeMap<>();
        for (Booking booking : bookings) {
            String month = booking.getDateTime().getMonth().getDisplayName(TextStyle.FULL, new Locale("es"));
            monthlyCount.merge(month, 1L, Long::sum);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("months", monthlyCount.keySet());
        result.put("counts", monthlyCount.values());
        return result;
    }

    @Override
    public long getTotalWins(String userId) {
        return matchResultRepository.findByWinnerId(userId).size();
    }

    @Override
    public long getTotalLosses(String userId) {
        // Obtenemos todas las reservas completadas del usuario
        List<Booking> userBookings = bookingRepository.findByPlayer1IdOrPlayer2Id(userId, userId);
        long completedMatches = userBookings.stream().filter(Booking::isFull).count();
        
        // Restamos las victorias para obtener las derrotas
        long wins = getTotalWins(userId);
        return completedMatches - wins;
    }

    @Override
    public long getTotalMatches(String userId) {
        List<Booking> userBookings = bookingRepository.findByPlayer1IdOrPlayer2Id(userId, userId);
        return userBookings.stream().filter(Booking::isFull).count();
    }

    @Override
    public String getFavoriteSport(String userId) {
        // Como solo hay squash por ahora, devolvemos "Squash"
        // En el futuro se puede calcular basado en las reservas
        return "Squash";
    }

    @Override
    public Map<String, Object> getPlayerRanking(String userId) {
        // Obtener todos los resultados para calcular ranking
        List<MatchResult> allResults = matchResultRepository.findAll();
        
        // Contar victorias por jugador
        Map<String, Long> winsByPlayer = allResults.stream()
            .collect(Collectors.groupingBy(MatchResult::getWinnerId, Collectors.counting()));
        
        // Ordenar por victorias (descendente)
        List<Map.Entry<String, Long>> sortedRanking = winsByPlayer.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .collect(Collectors.toList());
        
        // Encontrar posición del usuario
        int position = 1;
        for (Map.Entry<String, Long> entry : sortedRanking) {
            if (entry.getKey().equals(userId)) {
                break;
            }
            position++;
        }
        
        long totalPlayers = userRepository.count();
        
        Map<String, Object> result = new HashMap<>();
        result.put("position", position);
        result.put("totalPlayers", totalPlayers);
        result.put("wins", getTotalWins(userId));
        return result;
    }

    @Override
    public Map<String, Object> getPlayerEvolution(String userId) {
        // Obtener victorias ordenadas por fecha
        List<MatchResult> wins = matchResultRepository.findByWinnerId(userId);
        
        // Agrupar por semana/mes
        Map<String, Long> evolution = new LinkedHashMap<>();
        
        // Simulamos últimas 4 semanas
        String[] weeks = {"Semana 1", "Semana 2", "Semana 3", "Semana 4"};
        int[] weeklyWins = new int[4];
        
        for (MatchResult win : wins) {
            if (win.getMatchDate() != null) {
                int dayOfMonth = win.getMatchDate().getDayOfMonth();
                int weekIndex = (dayOfMonth - 1) / 7;
                if (weekIndex < 4) {
                    weeklyWins[weekIndex]++;
                }
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("weeks", weeks);
        result.put("values", weeklyWins);
        return result;
    }

    // ESTADÍSTICAS GLOBALES (PARA ADMIN)
    
    @Override
    public Map<String, Double> getCourtOccupation() {
        List<Booking> allBookings = bookingRepository.findAll();
        
        Map<String, Long> courtCount = allBookings.stream()
            .collect(Collectors.groupingBy(Booking::getCourtName, Collectors.counting()));
        
        long total = allBookings.size();
        
        Map<String, Double> occupation = new HashMap<>();
        courtCount.forEach((court, count) -> 
            occupation.put(court, total > 0 ? (count * 100.0 / total) : 0));
        
        return occupation;
    }

    @Override
    public Map<String, Long> getMostUsedSports() {
        Map<String, Long> sports = new HashMap<>();
        sports.put("Squash", bookingRepository.count());
        return sports;
    }

    @Override
    public Map<String, Long> getMonthlyBookingEvolution() {
        List<Booking> allBookings = bookingRepository.findAll();
        
        Map<String, Long> monthly = new TreeMap<>();
        for (Booking booking : allBookings) {
            String month = booking.getDateTime().getMonth().getDisplayName(TextStyle.FULL, new Locale("es"));
            monthly.merge(month, 1L, Long::sum);
        }
        
        return monthly;
    }

    @Override
    public Map<String, Long> getTopActivePlayers() {
        List<Booking> allBookings = bookingRepository.findAll();
        
        // Contar reservas por jugador
        Map<String, Long> playerBookings = new HashMap<>();
        for (Booking booking : allBookings) {
            playerBookings.merge(booking.getPlayer1Id(), 1L, Long::sum);
            if (booking.getPlayer2Id() != null) {
                playerBookings.merge(booking.getPlayer2Id(), 1L, Long::sum);
            }
        }
        
        // Top 10
        return playerBookings.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(10)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, 
                     (e1, e2) -> e1, LinkedHashMap::new));
    }
}