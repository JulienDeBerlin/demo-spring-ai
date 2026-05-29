package de.gematik.demospringai.adapter.in.web.dto;

import de.gematik.demospringai.domain.model.ModerationCategory;

import java.util.List;

public record ErrorResponse(
        String message,
        List<ModerationCategory> categories
) {
}

