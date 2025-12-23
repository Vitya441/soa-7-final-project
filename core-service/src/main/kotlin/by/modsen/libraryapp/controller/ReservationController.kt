package by.modsen.libraryapp.controller

import by.modsen.libraryapp.dto.response.ReservationResponse
import by.modsen.libraryapp.service.ReservationService
import by.modsen.libraryapp.util.SecurityUtils
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reservations")
class ReservationController(private val reservationService: ReservationService) {

    // todo: Протестировать все эндпоинты и потом пилить Front-End
    @PostMapping
    fun create(@RequestParam bookId: Long): ResponseEntity<ReservationResponse> {
        return ResponseEntity.ok(reservationService.reserveBook(bookId))
    }

    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    fun confirm(@PathVariable id: Long) {
        reservationService.confirmReservation(id)
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    fun reject(@PathVariable id: Long) {
        reservationService.rejectReservation(id)
    }

    @GetMapping("/my")
    fun getMyReservations(): List<ReservationResponse> {
        val currentUserId = extractUserId()
        return reservationService.getReservationsByReaderId(currentUserId)
    }

    @GetMapping("/readers/{readerId}")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    fun getByReaderId(@PathVariable readerId: Long): List<ReservationResponse> {
        return reservationService.getReservationsByReaderId(readerId)
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    fun getAll(): List<ReservationResponse> {
        return reservationService.getAllReservations();
    }

    private fun extractUserId(): Long {
        return SecurityUtils.getCurrentUserId()
            ?: throw RuntimeException("Не удалось определить пользователя")
    }
}