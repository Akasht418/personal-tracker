package com.akash.personalTracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class TransactionRequest {
    @NotNull
    @Positive(message = "Amount must be greater than 0")
    Double amount;

    @NotBlank
    @Pattern(regexp = "INCOME|EXPENSE", message = "Type must be either INCOME or EXPENSE")
    String type;

    @NotBlank(message = "Category cannot be blank")
    String category;
    LocalDate date;


}
