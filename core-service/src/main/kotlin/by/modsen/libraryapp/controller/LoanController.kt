package by.modsen.libraryapp.controller

import by.modsen.libraryapp.dto.request.LoanRequest
import by.modsen.libraryapp.dto.response.LoanResponse
import by.modsen.libraryapp.service.LoanService
import by.modsen.libraryapp.util.SecurityUtils
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/loans")
class LoanController(private val loanService: LoanService) {

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    fun create(@RequestBody loanRequest: LoanRequest): ResponseEntity<LoanResponse> {
        return ResponseEntity.ok(loanService.createLoan(loanRequest))
    }

    @PutMapping("/{loanId}/complete")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    fun complete(@PathVariable loanId: Long): ResponseEntity<LoanResponse> {
        return ResponseEntity.ok(loanService.completeLoan(loanId))
    }

    @GetMapping("/my")
    fun getMyLoans(): ResponseEntity<List<LoanResponse>> {
        val currentUserId = extractUserId()
        return ResponseEntity.ok(loanService.getLoansByReaderId(currentUserId))
    }

    // todo: Можно проверять readerId, делая REST вызов на auth-service
    @GetMapping("/readers/{readerId}")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    fun getByReaderId(@PathVariable readerId: Long): ResponseEntity<List<LoanResponse>> {
        return ResponseEntity.ok(loanService.getLoansByReaderId(readerId))
    }

    @GetMapping("/my/active")
    fun getMyActiveLoans(): ResponseEntity<List<LoanResponse>> {
        val currentUserId = extractUserId()
        return ResponseEntity.ok(loanService.getActiveLoansByReaderId(currentUserId))
    }

    // todo: Можно проверять readerId, делая REST вызов на auth-service
    @GetMapping("/readers/{readerId}/active")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    fun getActiveByReaderId(@PathVariable readerId: Long): ResponseEntity<List<LoanResponse>> {
        return ResponseEntity.ok(loanService.getActiveLoansByReaderId(readerId))
    }

    private fun extractUserId(): Long {
        return SecurityUtils.getCurrentUserId()
            ?: throw RuntimeException("Не удалось определить пользователя")
    }
}