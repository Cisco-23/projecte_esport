package com.company.projecte_esport.serviceImpl;

/**
 *
 * @author Leomar
 */

import com.company.projecte_esport.dto.BookingDTO;
import com.company.projecte_esport.model.Booking;
import com.company.projecte_esport.repository.BookingRepository;
import com.company.projecte_esport.service.BookingService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingDTO createIndividualBooking(BookingDTO dto) {
        // Buscar si alguien espera pareja a esa misma hora 
        return bookingRepository.findByDateTimeAndIsFullFalse(dto.getDateTime())
            .map(existingBooking -> {
                existingBooking.setPlayer2Id(dto.getPlayer1Id());
                existingBooking.setFull(true); // Se completa la pareja [cite: 15]
                return mapToDTO(bookingRepository.save(existingBooking));
            })
            .orElseGet(() -> {
                // Si no hay nadie, se crea la reserva esperando pareja 
                Booking newBooking = new Booking(dto.getDateTime(), dto.getCourtName(), 
                                                dto.getPlayer1Id(), null, false);
                return mapToDTO(bookingRepository.save(newBooking));
            });
    }

    @Override
    public BookingDTO createBookingWithPartner(BookingDTO dto) {
        // Reserva para dos personas ya registradas
        Booking booking = new Booking(dto.getDateTime(), dto.getCourtName(), 
                                      dto.getPlayer1Id(), dto.getPlayer2Id(), true);
        return mapToDTO(bookingRepository.save(booking));
    }

    @Override
    public List<BookingDTO> getUserBookings(String userId) {
        // Consultar historico de reservas del usuario
        return bookingRepository.findByPlayer1IdOrPlayer2Id(userId, userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelBooking(String bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    private BookingDTO mapToDTO(Booking b) {
        return new BookingDTO(b.getId(), b.getDateTime(), b.getCourtName(), 
                              b.getPlayer1Id(), b.getPlayer2Id(), b.isFull());
    }
}
