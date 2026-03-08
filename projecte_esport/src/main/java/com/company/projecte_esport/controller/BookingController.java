package com.company.projecte_esport.controller;

import com.company.projecte_esport.dto.BookingDTO;
import com.company.projecte_esport.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/individual")
    public ResponseEntity<BookingDTO> createIndividual(@RequestBody BookingDTO bookingDTO) {
        return ResponseEntity.ok(bookingService.createIndividualBooking(bookingDTO));
    }

    @PostMapping("/with-partner")
    public ResponseEntity<BookingDTO> createWithPartner(@RequestBody BookingDTO bookingDTO) {
        return ResponseEntity.ok(bookingService.createBookingWithPartner(bookingDTO));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDTO>> getUserBookings(@PathVariable String userId) {
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<BookingDTO>> getBookingsByDate(@PathVariable String date) {
        java.time.LocalDate localDate = java.time.LocalDate.parse(date);
        return ResponseEntity.ok(bookingService.getBookingsByDate(localDate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable String id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
}
