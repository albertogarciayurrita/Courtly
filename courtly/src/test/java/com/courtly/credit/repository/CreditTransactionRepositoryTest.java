package com.courtly.credit.repository;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.courtly.credit.entity.CreditTransaction;
import com.courtly.credit.entity.CreditTransactionType;
import com.courtly.user.entity.User;
import com.courtly.user.repository.UserRepository;

import jakarta.persistence.EntityManager;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CreditTransactionRepositoryTest {
    
    @Autowired
    private CreditTransactionRepository creditTransactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveAndFindCreditTransaction(){

        User user = new User("credit-user",
                "credit-user@courtly.com",
                "encoded-password-placeholder");

        User savedUser = userRepository.saveAndFlush(user);

        CreditTransaction creditTransaction = new CreditTransaction(savedUser,-10, CreditTransactionType.RESERVATION);

        CreditTransaction savedCreditTransaction = creditTransactionRepository.saveAndFlush(creditTransaction);

        Long transactionId = savedCreditTransaction.getId();
        Long userId = savedUser.getId();

        entityManager.clear();

        Optional<CreditTransaction> foundTransaction = creditTransactionRepository.findById(transactionId);

        assertThat(transactionId).isNotNull();
        assertThat(foundTransaction).isPresent();

        CreditTransaction retrievedTransaction = foundTransaction.orElseThrow();

        assertThat(retrievedTransaction.getId()).isEqualTo(transactionId);
        assertThat(retrievedTransaction.getAmount()).isEqualTo(-10);
        assertThat(retrievedTransaction.getType()).isEqualTo(CreditTransactionType.RESERVATION);
        assertThat(retrievedTransaction.getCreatedAt()).isNotNull();
        assertThat(retrievedTransaction.getUser().getId()).isEqualTo(userId);
    }
}
