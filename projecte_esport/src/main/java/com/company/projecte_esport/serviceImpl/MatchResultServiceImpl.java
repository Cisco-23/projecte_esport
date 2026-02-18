package com.company.projecte_esport.serviceImpl;

/**
 *
 * @author Leomar
 */

import com.company.projecte_esport.dto.MatchResultDTO;
import com.company.projecte_esport.model.MatchResult;
import com.company.projecte_esport.repository.MatchResultRepository;
import com.company.projecte_esport.service.MatchResultService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MatchResultServiceImpl implements MatchResultService {

    private final MatchResultRepository matchResultRepository;

    public MatchResultServiceImpl(MatchResultRepository matchResultRepository) {
        this.matchResultRepository = matchResultRepository;
    }

    @Override
    public MatchResultDTO createMatchResult(MatchResultDTO dto) {
        // Guardar resultado de la partida
        MatchResult result = new MatchResult(dto.getBookingId(), dto.getPlayer1Sets(), 
                                            dto.getPlayer2Sets(), dto.getWinnerId(), dto.getMatchDate());
        MatchResult saved = matchResultRepository.save(result);
        return new MatchResultDTO(saved.getBookingId(), saved.getPlayer1Sets(), 
                                 saved.getPlayer2Sets(), saved.getWinnerId(), saved.getMatchDate());
    }

    @Override
    public List<MatchResultDTO> getWinsByPlayerId(String playerId) {
        // Obtener estadisticas de victorias
        return matchResultRepository.findByWinnerId(playerId).stream()
                .map(m -> new MatchResultDTO(m.getBookingId(), m.getPlayer1Sets(), 
                                            m.getPlayer2Sets(), m.getWinnerId(), m.getMatchDate()))
                .toList();
    }
    
    @Override
    public MatchResultDTO getResultById(String id) {
        // Buscar un resultado especIfico por su ID
        return matchResultRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Resultado no encontrado con ID: " + id));
    }

    @Override
    public MatchResultDTO getResultByBookingId(String bookingId) {
        // Buscar el resultado vinculado a una reserva concreta
        return matchResultRepository.findByBookingId(bookingId)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("No hay resultado para la reserva: " + bookingId));
    }

    @Override
    public void deleteResult(String id) {
        // Eliminar un resultado de la base de datos
        if (!matchResultRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: ID no encontrado");
        }
        matchResultRepository.deleteById(id);
    }

    // MEtodo auxiliar para convertir el modelo MatchResult en DTO
    private MatchResultDTO mapToDTO(MatchResult m) {
        return new MatchResultDTO(
            m.getBookingId(), 
            m.getPlayer1Sets(), 
            m.getPlayer2Sets(), 
            m.getWinnerId(), 
            m.getMatchDate()
        );
    }
}
