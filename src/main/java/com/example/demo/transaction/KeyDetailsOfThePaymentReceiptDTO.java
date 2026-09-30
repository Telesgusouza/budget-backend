package com.example.demo.transaction;

import java.time.Instant;

public record KeyDetailsOfThePaymentReceiptDTO(String name, Float value, Instant date) {

}
