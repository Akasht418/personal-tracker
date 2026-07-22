package com.akash.personalTracker.services;

import com.akash.personalTracker.entity.User;
import com.akash.personalTracker.repository.TransactionRepository;
import com.akash.personalTracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;


    @Test
    void calculateNetBalanceByEmail_Success() {
        // ----------------------------------------------------
        // 1. GIVEN (Arrange): Setup test data and mock answers
        // ----------------------------------------------------
        String email = "coder1@example.com";

        // Create a fake User entity
        User fakeUser = new User();
        fakeUser.setUserId(1);
        fakeUser.setEmail(email);

        // Tell fake userRepository: when asked for "coder1@example.com", return fakeUser
        when(userRepository.findByEmail(email))
                .thenReturn(java.util.Optional.of(fakeUser));

        // Tell fake transactionRepository: return income = 1500.0 and expense = 500.0
        when(transactionRepository.sumAmountByUserIdAndType(1, "INCOME"))
                .thenReturn(1500.0);
        when(transactionRepository.sumAmountByUserIdAndType(1, "EXPENSE"))
                .thenReturn(500.0);

        // ----------------------------------------------------
        // 2. WHEN (Act): Call the actual method being tested
        // ----------------------------------------------------
        Double netBalance = transactionService.calculateNetBalanceByEmail(email);

        // ----------------------------------------------------
        // 3. THEN (Assert): Verify the output is 1000.0 (1500.0 - 500.0)
        // ----------------------------------------------------
        assertEquals(1000.0, netBalance);
    }

    @Test
    void calculateNetBalanceByEmail_UserNotFound_ThrowsException() {
        // ----------------------------------------------------
        // 1. GIVEN (Arrange)
        // ----------------------------------------------------
        String email = "missing@example.com";

        // Tell fake userRepository to return Optional.empty() (user doesn't exist)
        when(userRepository.findByEmail(email))
                .thenReturn(java.util.Optional.empty());

        // ----------------------------------------------------
        // 2. WHEN & 3. THEN (Act & Assert)
        // ----------------------------------------------------
        // Verify that calling the method throws UserNotFoundException
        org.junit.jupiter.api.Assertions.assertThrows(
                com.akash.personalTracker.Exceptions.UserNotFoundException.class,
                () -> transactionService.calculateNetBalanceByEmail(email)
        );

        // Verify that transactionRepository was NEVER called
        org.mockito.Mockito.verify(transactionRepository, org.mockito.Mockito.never())
                .sumAmountByUserIdAndType(org.mockito.Mockito.anyInt(), org.mockito.Mockito.anyString());
    }


    @Test
    void calculateNetBalanceByEmail_NullSums_ReturnsZero() {
        // ----------------------------------------------------
        // 1. GIVEN (Arrange)
        // ----------------------------------------------------
        String email = "newuser@example.com";

        User fakeUser = new User();
        fakeUser.setUserId(2);
        fakeUser.setEmail(email);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(fakeUser));

        // Mock DB returning null when there are no transactions
        when(transactionRepository.sumAmountByUserIdAndType(2, "INCOME"))
                .thenReturn(null);
        when(transactionRepository.sumAmountByUserIdAndType(2, "EXPENSE"))
                .thenReturn(null);

        // ----------------------------------------------------
        // 2. WHEN (Act)
        // ----------------------------------------------------
        Double netBalance = transactionService.calculateNetBalanceByEmail(email);

        // ----------------------------------------------------
        // 3. THEN (Assert)
        // ----------------------------------------------------
        assertEquals(0.0, netBalance);
    }

}
