package com.example.hunter_point.config.consumer;

import com.example.hunter_point.config.RabbitMQConfig;
import com.example.hunter_point.service.impl.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionConsumer {

    private final TelegramService telegramService;

    @RabbitListener(queues = RabbitMQConfig.TRANSACTION_QUEUE)
    public void handleTransactionMessage(String message) {
        try {
            System.out.println("📩 Received message from RabbitMQ: " + message);
            telegramService.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace(); // log ra lỗi cụ thể
        }
    }
}

