package com.padel.draft.api;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record CreateLeagueRequest(
        @NotBlank(message = "name is required") String name,
        @DecimalMin(value = "0.01", message = "salaryCap must be greater than zero") BigDecimal salaryCap,
        @Min(value = 1, message = "rosterSize must be at least 1")
        @Max(value = 30, message = "rosterSize cannot exceed 30") int rosterSize) { }
