package de.gematik.demospringai.domain.model;

import java.util.UUID;

public record Profile(
        String id,
        String name,
        int age,
        String bio
) {
    public static Profile create(String displayName, int age, String bio) {
        return new Profile(UUID.randomUUID().toString(), displayName, age, bio);
    }
}

