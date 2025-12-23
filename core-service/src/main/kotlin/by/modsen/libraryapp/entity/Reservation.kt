package by.modsen.libraryapp.entity

import by.modsen.libraryapp.enumeration.OrderStatus
import by.modsen.libraryapp.util.OrderStatusConverter
import by.modsen.libraryapp.util.SecurityUtils
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PrePersist
import java.time.LocalDate

@Entity
class Reservation(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    val book: Book,

    var readerId: Long?,

    val reservationDate: LocalDate = LocalDate.now(),

    @Convert(converter = OrderStatusConverter::class)
    var status: OrderStatus = OrderStatus.PENDING

) : BaseEntity() {

    @PrePersist
    private fun prePersist() {
        if (this.readerId == null) {
            this.readerId = SecurityUtils.getCurrentUserId()
                ?: throw IllegalStateException("Не удалось определить ID пользователя для бронирования")
        }
    }
}
