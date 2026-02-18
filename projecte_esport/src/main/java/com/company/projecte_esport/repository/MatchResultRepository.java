package com.company.projecte_esport.repository;
/**
 *
 * @author Leomar
 */

import com.company.projecte_esport.model.MatchResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

// Repositorio para los resultados de los partidos y estadisticas
public interface MatchResultRepository extends MongoRepository<MatchResult, String> {
    // Buscar todos los resultados asociados a un jugador
    List<MatchResult> findByWinnerId(String winnerId);
    
    // Buscar el resultado de una reserva específica
    Optional<MatchResult> findByBookingId(String bookingId);
    
    // Verificar si ya existe resultado para una reserva
    boolean existsByBookingId(String bookingId);
}