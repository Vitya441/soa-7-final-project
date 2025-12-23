package by.modsen.libraryapp.entity

import by.modsen.libraryapp.util.SecurityUtils
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PrePersist
import java.time.LocalDate

@Entity
class Loan(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    val book: Book,

    var readerId: Long?,

    val loanDate: LocalDate = LocalDate.now(),

    val dueDate: LocalDate,

    var isReturned: Boolean = false
) : BaseEntity() {

    @PrePersist
    private fun prePersist() {
        if (this.readerId == null) {
            this.readerId = SecurityUtils.getCurrentUserId()
                ?: throw IllegalStateException("Не удалось определить ID пользователя для бронирования")
        }
    }
}
