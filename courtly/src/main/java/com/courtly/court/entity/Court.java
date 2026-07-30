package com.courtly.court.entity;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.ISBN;

import com.courtly.facility.entity.Facility;

import jakarta.persistence.FetchType;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "courts")
public class Court {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "credits_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal creditCost;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    protected Court() {
        // Default constructor for JPA
    }

    public Court(String name, String description, BigDecimal creditCost, boolean active, Facility facility) {
        this.name = name;
        this.description = description;
        this.creditCost = creditCost;
        this.active = active;
        this.facility = facility;
    }

    public Long getId() {
        return id;
    }   

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCreditCost() {
        return creditCost;
    }

    public void setCreditCost(BigDecimal creditCost) {
        this.creditCost = creditCost;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    @Override
    public String toString() {
        return "Court{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", creditCost=" + creditCost +
                ", active=" + active +
                ", facility=" + (facility != null ? facility.getId() : null) +
                '}';
    }
}
