package de.gematik.demospringai.adapter.out.persistence;

import de.gematik.demospringai.application.port.out.ProfileRepository;
import de.gematik.demospringai.domain.model.Profile;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryProfileRepository implements ProfileRepository {

    private final Map<String, Profile> store = new ConcurrentHashMap<>();

    @Override
    public Profile save(Profile profile) {
        store.put(profile.id(), profile);
        return profile;
    }
}

