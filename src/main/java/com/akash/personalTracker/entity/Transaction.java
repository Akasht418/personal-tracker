package com.akash.personalTracker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer transaction_id;
    Double amount;
    String type;
    String category;
    LocalDate date;

    @ManyToOne
    @JoinColumn(name="user_id")
    User user;
}
