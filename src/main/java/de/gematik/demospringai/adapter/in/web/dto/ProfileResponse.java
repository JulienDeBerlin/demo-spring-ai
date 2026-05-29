package de.gematik.demospringai.adapter.in.web.dto;

public record ProfileResponse(
        String id,
        String name,
        int age,
        String bio
) {
}

