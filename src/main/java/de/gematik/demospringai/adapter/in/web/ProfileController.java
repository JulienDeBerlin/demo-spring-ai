package de.gematik.demospringai.adapter.in.web;

import de.gematik.demospringai.adapter.in.web.dto.CreateProfileRequest;
import de.gematik.demospringai.adapter.in.web.dto.ProfileResponse;
import de.gematik.demospringai.application.port.in.CreateProfileUseCase;
import de.gematik.demospringai.domain.model.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final CreateProfileUseCase createProfileUseCase;

    @PostMapping
    public ResponseEntity<ProfileResponse> createProfile(@Valid @RequestBody CreateProfileRequest request) {
        log.info("Received profile creation request for name='{}', age={}", request.name(), request.age());

        Profile profile = createProfileUseCase.createProfile(request.name(), request.age(), request.bio());

        ProfileResponse response = new ProfileResponse(
                profile.id(),
                profile.name(),
                profile.age(),
                profile.bio()
        );

        log.info("Profile created successfully: id={}", profile.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

