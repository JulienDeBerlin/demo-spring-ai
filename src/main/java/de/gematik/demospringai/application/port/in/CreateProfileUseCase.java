package de.gematik.demospringai.application.port.in;

import de.gematik.demospringai.domain.model.Profile;

public interface CreateProfileUseCase {
    Profile createProfile(String displayName, int age, String bio);
}

