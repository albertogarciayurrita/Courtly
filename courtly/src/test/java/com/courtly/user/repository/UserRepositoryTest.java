package com.courtly.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.courtly.user.entity.Role;
import com.courtly.user.entity.User;

import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveAndFindUserByEmail() {
        User user = new User(
                "roberto",
                "roberto@courtly.com",
                "encoded-password-placeholder");

        User savedUser = userRepository.saveAndFlush(user);

        Long savedUserId = savedUser.getId();

        entityManager.clear();

        Optional<User> foundUser = userRepository.findByEmail("roberto@courtly.com");

        assertThat(savedUserId).isNotNull();
        assertThat(foundUser).isPresent();

        User retrievedUser = foundUser.orElseThrow();

        assertThat(retrievedUser.getId()).isEqualTo(savedUserId);
        assertThat(retrievedUser.getUsername()).isEqualTo("roberto");
        assertThat(retrievedUser.getEmail()).isEqualTo("roberto@courtly.com");
        assertThat(retrievedUser.getPassword()).isEqualTo("encoded-password-placeholder");
        assertThat(retrievedUser.getCredits()).isEqualTo(60);
        assertThat(retrievedUser.getRole()).isEqualTo(Role.USER);
        assertThat(retrievedUser.getCreatedAt()).isNotNull();
    }
}
