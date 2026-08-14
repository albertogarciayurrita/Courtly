package com.courtly.court.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CourtRequest(
    @NotBlank(message = "Court name must not be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    String name,

    @Size(max = 500, message = "Description must not exceed 500 characters")
    String description,

    @NotNull(message = "Credit cost must not be null")
    @Positive(message = "Credit cost must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Credit cost must have at most 8 integer digits and 2 decimal digits")
    int creditCost,

    @NotNull(message = "Active status must not be null")
    Boolean active
){
    
}
