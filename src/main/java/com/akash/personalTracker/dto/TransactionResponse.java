package com.akash.personalTracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TransactionResponse {
    Integer transaction_id;
    Double amount;
    String type;
    String category;
    LocalDate date;
    Integer user_id;
}
