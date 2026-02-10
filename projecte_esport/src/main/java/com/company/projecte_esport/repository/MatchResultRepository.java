package com.company.projecte_esport.repository;

import com.company.projecte_esport.model.MatchResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

// Repositorio para los resultados de los partidos y estadisticas
public interface MatchResultRepository extends MongoRepository<MatchResult, String> {
    // Buscar todos los resultados asociados a un jugador
    List<MatchResult> findByWinnerId(String winnerId);
}