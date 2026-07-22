package com.akash.personalTracker.services;

import com.akash.personalTracker.Exceptions.UserNotFoundException;
import com.akash.personalTracker.dto.TransactionRequest;
import com.akash.personalTracker.dto.TransactionResponse;
import com.akash.personalTracker.entity.Transaction;
import com.akash.personalTracker.entity.User;
import com.akash.personalTracker.repository.TransactionRepository;
import com.akash.personalTracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public TransactionResponse createTransaction(TransactionRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setCategory(request.getCategory());
        transaction.setDate(request.getDate());
        transaction.setType(request.getType());
        transaction.setUser(user);

        Transaction saved = transactionRepository.save(transaction);
        return mapToResponse(saved);
    }

    public List<TransactionResponse> findTransactionsByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        List<Transaction> transactions = transactionRepository.findByUserId(user.getUserId());
        return transactions.stream().map(this::mapToResponse).toList();
    }

    public Double calculateNetBalanceByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        Double totalIncome = transactionRepository.sumAmountByUserIdAndType(user.getUserId(), "INCOME");
        Double totalExpense = transactionRepository.sumAmountByUserIdAndType(user.getUserId(), "EXPENSE");

        double income = (totalIncome != null) ? totalIncome : 0.0;
        double expense = (totalExpense != null) ? totalExpense : 0.0;

        return income - expense;
    }

    // Helper mapper method to keep code DRY (Don't Repeat Yourself)
    private TransactionResponse mapToResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setTransaction_id(transaction.getTransaction_id());
        response.setAmount(transaction.getAmount());
        response.setCategory(transaction.getCategory());
        response.setDate(transaction.getDate());
        response.setType(transaction.getType()); // Fixed typo here!
        response.setUser_id(transaction.getUser().getUserId());
        return response;
    }
}