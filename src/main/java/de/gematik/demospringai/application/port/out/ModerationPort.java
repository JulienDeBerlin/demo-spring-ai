package de.gematik.demospringai.application.port.out;

import de.gematik.demospringai.domain.model.ModerationResult;

public interface ModerationPort {
    ModerationResult moderate(String text);
}

