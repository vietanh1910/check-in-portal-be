package com.example.hunter_point.controller;

import com.example.hunter_point.dto.request.TransactionRequest;
import com.example.hunter_point.dto.response.TransactionResponse;
import com.example.hunter_point.service.TransactionService;
import com.example.hunter_point.utils.response.GenerateResponse;
import com.example.hunter_point.utils.response.ListResponse;
import com.example.hunter_point.utils.response.SimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ALLOCATOR')")
    public ListResponse<TransactionResponse> getTransactions(@RequestParam int page, @RequestParam int size) {
        try {
            return transactionService.getTransactionsByUser(page, size);
        } catch (Exception e){
            e.printStackTrace();
            return GenerateResponse.generateErrorListResponse("BAD_REQUEST");
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ALLOCATOR')")
    public SimpleResponse createTransaction(@RequestBody TransactionRequest request) {
        try {
            return transactionService.createTransaction(request);
        } catch (Exception e){
            e.printStackTrace();
            return GenerateResponse.generateErrorSimpleResponse("BAD_REQUEST");
        }
    }

    @PostMapping("/{transactionId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public SimpleResponse approveTransaction(@PathVariable Long transactionId) {
        try {
            return transactionService.approveTransaction(transactionId);
        } catch (Exception e){
            e.printStackTrace();
            return GenerateResponse.generateErrorSimpleResponse("BAD_REQUEST");
        }
    }

}

