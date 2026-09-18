package com.example.demo.transaction;

import com.example.demo.budget.CategoryEnum;

public record otherPaymentReceiptDetailsDTO(boolean statusTransaction, CategoryEnum category) {
}
