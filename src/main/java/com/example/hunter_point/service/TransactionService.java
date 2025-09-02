package com.example.hunter_point.service;

import com.example.hunter_point.dto.request.TransactionRequest;
import com.example.hunter_point.dto.response.TransactionResponse;
import com.example.hunter_point.utils.response.GetDetailResponse;
import com.example.hunter_point.utils.response.ListResponse;
import com.example.hunter_point.utils.response.SimpleResponse;

public interface TransactionService {
    ListResponse<TransactionResponse> getTransactionsByUser(int page, int size);

    GetDetailResponse<TransactionResponse> createTransaction(TransactionRequest request);

    SimpleResponse approveTransaction(Long transactionId);
}
