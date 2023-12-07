package org.test.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.test.domain.Contestants;

import java.util.Optional;

@ApplicationScoped
public class ContestantsRepository implements PanacheRepository<Contestants> {

    public Optional<Contestants> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }
}
