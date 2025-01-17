package com.easyscore.controller;


import com.easyscore.model.Booking;
import com.easyscore.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Operation(summary = "Lista todas las reservas")
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.findAll();
    }

    @Operation(summary = "Crea una nueva reserva")
    @PostMapping
    public ResponseEntity<String> createBooking(@RequestBody Booking booking) {
        LocalTime startTime = booking.getHoraInicio();
        LocalTime endTime = booking.getHoraFin();

        if (bookingService.isProductAvailable(booking.getProducto().getId(), booking.getFechaReserva(), startTime, endTime)) {
            bookingService.save(booking);
            return ResponseEntity.ok("Booking created successfully");
        } else {
            return ResponseEntity.status(409).body("Product is not available for the selected time slot");
        }
    }

    @Operation(summary = "Elimina/Cancela una reserva")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        try {
            bookingService.delete(id);
            return ResponseEntity.ok("Reserva cancelada con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al cancelar la reserva");
        }
    }

    @Operation(summary = "Busca reservas por producto y fecha")
    @GetMapping("/product/{productId}/date/{date}")
    public List<Booking> getBookingsByProductAndDate(@PathVariable Long productId, @PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return bookingService.findBookingsByProductoAndFecha(productId, localDate);
    }
}

