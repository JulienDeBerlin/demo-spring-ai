package de.gematik.demospringai.application.service;

import de.gematik.demospringai.application.port.in.CreateProfileUseCase;
import de.gematik.demospringai.application.port.out.ModerationPort;
import de.gematik.demospringai.application.port.out.ProfileRepository;
import de.gematik.demospringai.domain.exception.ProfileRejectedException;
import de.gematik.demospringai.domain.model.ModerationResult;
import de.gematik.demospringai.domain.model.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService implements CreateProfileUseCase {

    private final ModerationPort moderationPort;
    private final ProfileRepository profileRepository;

    @Override
    public Profile createProfile(String displayName, int age, String bio) {
        ModerationResult result = moderationPort.moderate(bio);

        if (!result.allowed()) {
            throw new ProfileRejectedException(result);
        }

        Profile profile = Profile.create(displayName, age, bio);
        return profileRepository.save(profile);
    }
}

