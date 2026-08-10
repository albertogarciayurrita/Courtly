package com.courtly.user.entity;

import java.time.Instant;

import com.courtly.credit.exception.InsufficientCreditsException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 72)
    private String password;

    @Column(nullable = false)
    private int credits = 60;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = true, length = 20)
    private Role role = Role.USER;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        if(credits < 0){
            throw new IllegalArgumentException("Credits must be greater than 0");
        }
        this.credits = credits;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", credits=" + credits +
                ", role=" + role +
                ", createdAt=" + createdAt +
                '}';
    }

    public void deductCredits(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to deduct must be greater than 0");
        }
        if (this.credits < amount) {
            throw new InsufficientCreditsException("Insufficient credits. Available: " + this.credits + ", " + "required: " + amount);
        }

        this.credits -= amount;
    }

    public void addCredits(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to refund must be greater than 0");
        }
        this.credits += amount;
    }
}
