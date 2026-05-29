package de.gematik.demospringai.adapter.in.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProfileRequest(
        @NotBlank(message = "Display name must not be blank")
        String name,

        @Min(value = 18, message = "Age must be at least 18")
        @Max(value = 120, message = "Age must be at most 120")
        int age,

        @NotBlank(message = "Bio must not be blank")
        @Size(max = 1000, message = "Bio must not exceed 1000 characters")
        String bio
) {
}

