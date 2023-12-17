package org.test.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.test.domain.Contestants;
import org.test.domain.Courses;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CoursesRepository implements PanacheRepository<Courses> {

    public Optional<Courses> findByName(String name) {
        final String searchTerm = name.toUpperCase().toLowerCase();
        return find("name", searchTerm).firstResultOptional();
    }
}
