package de.gematik.demospringai.domain.exception;

import de.gematik.demospringai.domain.model.ModerationResult;
import lombok.Getter;


@Getter
public class ProfileRejectedException extends RuntimeException {

    private final transient ModerationResult moderationResult;

    public ProfileRejectedException(ModerationResult moderationResult) {
        super(moderationResult.reason());
        this.moderationResult = moderationResult;
    }

}

