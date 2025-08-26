package com.example.hunter_point.service;

import com.example.hunter_point.dto.response.TransactionResponse;
import com.example.hunter_point.utils.response.ListResponse;

public interface TransactionService {
    ListResponse<TransactionResponse> getTransactionsByUser(int page, int size);
}
