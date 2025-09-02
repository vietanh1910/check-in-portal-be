package com.example.hunter_point.entity.enums;

public enum TransactionType {
    SPENT("đã sử dụng"),
    TOPUP("nạp");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
