package de.gematik.demospringai.domain.model;

import java.io.Serializable;
import java.util.List;

public record ModerationResult(
        boolean allowed,
        List<ModerationCategory> categories,
        String reason
) implements Serializable {
}

