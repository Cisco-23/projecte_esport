package com.company.projecte_esport.service;

import com.company.projecte_esport.dto.BookingDTO;
import java.util.List;

public interface BookingService {
    BookingDTO createBookingWithPartner(BookingDTO bookingDTO);
    BookingDTO createIndividualBooking(BookingDTO bookingDTO);
    List<BookingDTO> getUserBookings(String userId);
    void cancelBooking(String bookingId);
    List<BookingDTO> getBookingsByDate(java.time.LocalDate date);
    List<BookingDTO> getPendingResults(String userId);
}