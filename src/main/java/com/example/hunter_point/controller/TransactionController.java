package com.example.hunter_point.controller;

import com.example.hunter_point.dto.response.TransactionResponse;
import com.example.hunter_point.service.TransactionService;
import com.example.hunter_point.utils.response.GenerateResponse;
import com.example.hunter_point.utils.response.ListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    @PreAuthorize("hasRole('ALLOCATOR')")
    public ListResponse<TransactionResponse> getTransactions(@RequestParam int page, @RequestParam int size) {
        try {
            return transactionService.getTransactionsByUser(page, size);
        } catch (Exception e){
            e.printStackTrace();
            return GenerateResponse.generateErrorListResponse("BAD_REQUEST");
        }
    }
}

