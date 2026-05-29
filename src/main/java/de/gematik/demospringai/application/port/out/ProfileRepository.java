package de.gematik.demospringai.application.port.out;

import de.gematik.demospringai.domain.model.Profile;

public interface ProfileRepository {
    Profile save(Profile profile);
}

