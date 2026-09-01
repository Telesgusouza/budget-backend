package com.example.demo.transaction;

import java.time.Instant;
import java.util.UUID;

public record TransactionsResponseHomeDTO(UUID id, String name, Instant date, Float amount, Boolean statusAmount) {

}
