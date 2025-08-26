package com.example.hunter_point.service.impl;

import com.example.hunter_point.config.RabbitMQConfig;
import com.example.hunter_point.dto.request.TransactionRequest;
import com.example.hunter_point.dto.response.TransactionResponse;
import com.example.hunter_point.entity.Transaction;
import com.example.hunter_point.entity.enums.ERole;
import com.example.hunter_point.entity.enums.TransactionStatus;
import com.example.hunter_point.repository.TransactionRepository;
import com.example.hunter_point.security.UserDetailsImpl;
import com.example.hunter_point.service.TransactionService;
import com.example.hunter_point.utils.response.GenerateResponse;
import com.example.hunter_point.utils.response.ListResponse;
import com.example.hunter_point.utils.response.SimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public ListResponse<TransactionResponse> getTransactionsByUser(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(page, size);

        Page<Transaction> pageResult;
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + ERole.ADMIN.name()))) {
            pageResult = transactionRepository.findAll(pageable);

        } else if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + ERole.ALLOCATOR.name()))) {
            pageResult = transactionRepository.findByUserId(userDetails.getId(), pageable);

        } else {
            return GenerateResponse.generateErrorListResponse("You don't have permission");
        }
        List<TransactionResponse> responseList = pageResult.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return GenerateResponse.generateSuccessListResponse(
                responseList,
                pageResult.getTotalElements());
    }

    @Override
    public SimpleResponse createTransaction(TransactionRequest request) {
        Transaction transaction = Transaction.builder()
                .type(request.getType())
                .point(request.getPoint())
                .amount(request.getAmount())
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .userId(request.getUserId())
                .status(TransactionStatus.PENDING)
                .build();

        Transaction saved = transactionRepository.save(transaction);

        // Gửi message vào RabbitMQ
        String message = "Tài khoản ID " + saved.getUserId()
                + " đã " + saved.getType().name().toLowerCase()
                + " số tiền: " + saved.getAmount()
                + " (point: " + saved.getPoint() + ")";
        rabbitTemplate.convertAndSend(RabbitMQConfig.TRANSACTION_QUEUE, message);

        return GenerateResponse.generateSuccessSimpleResponse();
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .type(transaction.getType())
                .point(transaction.getPoint())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt())
                .userId(transaction.getUserId())
                .status(transaction.getStatus())
                .build();
    }
}

