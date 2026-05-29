package de.gematik.demospringai.application.service;

import de.gematik.demospringai.application.port.in.CreateProfileUseCase;
import de.gematik.demospringai.application.port.out.ModerationPort;
import de.gematik.demospringai.application.port.out.ProfileRepository;
import de.gematik.demospringai.domain.exception.ProfileRejectedException;
import de.gematik.demospringai.domain.model.ModerationResult;
import de.gematik.demospringai.domain.model.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService implements CreateProfileUseCase {

    private final ModerationPort moderationPort;
    private final ProfileRepository profileRepository;

    @Override
    public Profile createProfile(String displayName, int age, String bio) {
        log.debug("Moderating bio for user '{}'", displayName);
        ModerationResult result = moderationPort.moderate(bio);

        if (!result.allowed()) {
            log.warn("Bio rejected for user '{}': reason='{}', categories={}", displayName, result.reason(), result.categories());
            throw new ProfileRejectedException(result);
        }

        log.debug("Bio approved for user '{}'", displayName);
        Profile profile = Profile.create(displayName, age, bio);
        return profileRepository.save(profile);
    }
}

