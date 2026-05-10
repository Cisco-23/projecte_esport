// serviceImpl/BookingServiceImpl.java
package com.company.projecte_esport.serviceImpl;

import com.company.projecte_esport.dto.BookingDTO;
import com.company.projecte_esport.model.Booking;
import com.company.projecte_esport.model.User;
import com.company.projecte_esport.repository.BookingRepository;
import com.company.projecte_esport.repository.MatchResultRepository;
import com.company.projecte_esport.repository.UserRepository;
import com.company.projecte_esport.service.BookingService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final MatchResultRepository matchResultRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, 
                             MatchResultRepository matchResultRepository,
                             UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.matchResultRepository = matchResultRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BookingDTO createIndividualBooking(BookingDTO dto) {
        System.out.println("========================================");
        System.out.println("🔍 CREANDO RESERVA INDIVIDUAL");
        System.out.println("   Fecha/Hora: " + dto.getDateTime());
        System.out.println("   Pista: " + dto.getCourtName());
        System.out.println("   Player1 ID: " + dto.getPlayer1Id());
        System.out.println("========================================");
        
        // Buscar si hay una reserva SIN COMPLETAR en la misma fecha, hora Y pista
        // que NO sea del mismo jugador
        List<Booking> allBookings = bookingRepository.findAll();
        
        System.out.println("📊 Reservas existentes: " + allBookings.size());
        for (Booking b : allBookings) {
            System.out.println("   - ID: " + b.getId() + 
                             " | Pista: " + b.getCourtName() + 
                             " | Fecha: " + b.getDateTime() + 
                             " | Player1: " + b.getPlayer1Id() + 
                             " | Player2: " + b.getPlayer2Id() + 
                             " | isFull: " + b.isFull());
        }
        
        // Buscar reserva compatible (misma fecha, misma pista, NO completada, jugador DIFERENTE)
        Optional<Booking> existingBooking = allBookings.stream()
            .filter(b -> b.getDateTime().equals(dto.getDateTime()))
            .filter(b -> b.getCourtName().equals(dto.getCourtName()))
            .filter(b -> !b.isFull())
            .filter(b -> !b.getPlayer1Id().equals(dto.getPlayer1Id())) // IMPORTANTE: jugador diferente
            .findFirst();
        
        if (existingBooking.isPresent()) {
            Booking booking = existingBooking.get();
            System.out.println("✅ Encontrada reserva compatible!");
            System.out.println("   Reserva ID: " + booking.getId());
            System.out.println("   Player1 (existente): " + booking.getPlayer1Id());
            System.out.println("   Player2 (nuevo): " + dto.getPlayer1Id());
            
            // Completar la reserva
            booking.setPlayer2Id(dto.getPlayer1Id());
            booking.setFull(true);
            Booking saved = bookingRepository.save(booking);
            
            System.out.println("✅ Reserva completada! ID: " + saved.getId());
            System.out.println("   isFull: " + saved.isFull());
            System.out.println("   Player1: " + saved.getPlayer1Id());
            System.out.println("   Player2: " + saved.getPlayer2Id());
            System.out.println("========================================");
            
            return mapToDTO(saved);
        } else {
            System.out.println("❌ No se encontró reserva compatible. Creando nueva...");
            
            // Crear nueva reserva esperando pareja
            Booking newBooking = new Booking(
                dto.getDateTime(), 
                dto.getCourtName(),
                dto.getPlayer1Id(), 
                null, 
                false
            );
            
            Booking saved = bookingRepository.save(newBooking);
            System.out.println("✅ Nueva reserva creada! ID: " + saved.getId());
            System.out.println("   Jugador: " + saved.getPlayer1Id());
            System.out.println("   Esperando rival...");
            System.out.println("========================================");
            
            return mapToDTO(saved);
        }
    }


@Override
public BookingDTO createBookingWithPartner(BookingDTO dto) {
    System.out.println("👥 RESERVA CON PAREJA");
    System.out.println("   Player1 ID: " + dto.getPlayer1Id());
    System.out.println("   Player2 Email/ID: " + dto.getPlayer2Id());
    
    String player1Id = dto.getPlayer1Id();
    String player2Id = dto.getPlayer2Id();
    
    if (player1Id.equals(player2Id)) {
        throw new RuntimeException("No puedes reservar contigo mismo");
    }
    
    String actualPlayer2Id = player2Id;
    
    if (player2Id.contains("@")) {
        User player2 = userRepository.findByEmail(player2Id)
            .orElseThrow(() -> new RuntimeException("No se encontró ningún usuario con el email: " + player2Id));
        actualPlayer2Id = player2.getId();
        System.out.println("   Email convertido a ID: " + actualPlayer2Id);
    }
    
    if (player1Id.equals(actualPlayer2Id)) {
        throw new RuntimeException("No puedes reservar contigo mismo");
    }
    
    Booking booking = new Booking(
        dto.getDateTime(), 
        dto.getCourtName(),
        player1Id, 
        actualPlayer2Id, 
        true
    );
    
    Booking saved = bookingRepository.save(booking);
    System.out.println("✅ Reserva con pareja creada! ID: " + saved.getId());
    
    return mapToDTO(saved);
}

    @Override
    public List<BookingDTO> getUserBookings(String userId) {
        System.out.println("📋 Cargando reservas para usuario: " + userId);
        
        List<Booking> bookings = bookingRepository.findByPlayer1IdOrPlayer2Id(userId, userId);
        System.out.println("   Encontradas: " + bookings.size() + " reservas");
        
        return bookings.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelBooking(String bookingId) {
        System.out.println("🗑️ Cancelando reserva: " + bookingId);
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public List<BookingDTO> getBookingsByDate(java.time.LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        return bookingRepository.findByDateTimeBetween(startOfDay, endOfDay).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private BookingDTO mapToDTO(Booking b) {
        return new BookingDTO(
            b.getId(), 
            b.getDateTime(), 
            b.getCourtName(),
            b.getPlayer1Id(), 
            b.getPlayer2Id(), 
            b.isFull()
        );
    }
    


@Override
public List<BookingDTO> getPendingResults(String userId) {
    List<Booking> allBookings = bookingRepository.findByPlayer1IdOrPlayer2Id(userId, userId);
    
    return allBookings.stream()
            .filter(Booking::isFull)
            .filter(b -> !matchResultRepository.existsByBookingId(b.getId()))
            .map(this::mapToDTO)
            .collect(Collectors.toList());
}
}