package by.modsen.libraryapp.service

import by.modsen.libraryapp.dto.response.ReservationResponse

interface ReservationService {

    fun reserveBook(bookId: Long): ReservationResponse

    fun rejectReservation(reservationId: Long)

    fun confirmReservation(reservationId: Long)

    fun getReservationsByReaderId(readerId: Long): List<ReservationResponse>

    fun getAllReservations(): List<ReservationResponse>
}
