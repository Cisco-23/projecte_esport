package com.company.projecte_esport.service;

/**
 *
 * @author Jesus
 */
import com.company.projecte_esport.dto.BookingDTO;
import java.util.List;

public interface BookingService {

    // Crear reserva sabiendo ya quién es la pareja
    BookingDTO createBookingWithPartner(BookingDTO bookingDTO);

    // Crear reserva individual (el sistema busca pareja)
    BookingDTO createIndividualBooking(BookingDTO bookingDTO);

    // Ver mis reservas
    List<BookingDTO> getUserBookings(String userId);

    // Cancelar
    void cancelBooking(String bookingId);
}
