/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.projecte_esport.service;

/**
 *
 * @author Jesus
 */

import com.company.projecte_esport.dto.MatchResultDTO;
import java.util.List;

public interface MatchResultService {
    
    // Guardar un nuevo resultado
    MatchResultDTO createMatchResult(MatchResultDTO resultDTO);

    // Obtener un resultado por su ID
    MatchResultDTO getResultById(String id);

    // Obtener resultado por ID de la reserva
    MatchResultDTO getResultByBookingId(String bookingId);

    // Obtener historial de victorias de un jugador
    List<MatchResultDTO> getWinsByPlayerId(String playerId);

    // Eliminar un resultado (si fuera necesario, ej: error de introducción)
    void deleteResult(String id);
}
