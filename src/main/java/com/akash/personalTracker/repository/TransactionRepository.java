package com.akash.personalTracker.repository;

import com.akash.personalTracker.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer > {
    @Query("SELECT t FROM Transaction t WHERE t.user.userId = :userId")
    List<Transaction> findByUserId(Integer userId);

    //List<Transaction> findByUserUserIdAndType(Integer user_id, String type);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.userId = :userId AND t.type = :type")
    Double sumAmountByUserIdAndType(Integer userId, String type);
}
