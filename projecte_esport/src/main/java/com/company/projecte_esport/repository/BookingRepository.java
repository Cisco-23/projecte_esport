package com.company.projecte_esport.repository;

import com.company.projecte_esport.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

// Repositorio para la gestion de reservas de squash
public interface BookingRepository extends MongoRepository<Booking, String> {
    // Buscar reservas donde el usuario es el jugador 1 o el jugador 2 (historico)
    List<Booking> findByPlayer1IdOrPlayer2Id(String player1Id, String player2Id);

   // Busca si hay hueco en una fecha y hora exactas
Optional<Booking> findByDateTimeAndIsFullFalse(LocalDateTime dateTime);
}