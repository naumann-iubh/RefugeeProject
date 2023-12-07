package org.test.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.test.domain.Contestants;
import org.test.domain.Courses;
import org.test.domain.Registration;
import org.test.exception.ServiceException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class RegistrationRepository implements PanacheRepository<Registration> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Inject
    ContestantsRepository contestantsRepository;

    @Inject
    CoursesRepository coursesRepository;

    public List<Contestants> getContestantsForCourse(Integer course_id) {
        final List<Registration> registrations = listAll();
        final List<Registration> courseRegistration = registrations.stream().filter(id -> id.getCourse().getCourse_id().equals(course_id)).toList();
        return courseRegistration.stream().map(Registration::getContestants).toList();
    }

    public boolean enroll(Integer course_id, Contestants contestants) throws ServiceException {

        final LocalDateTime now = LocalDateTime.now();

        final String formatDateTime = now.format(formatter);

        if (coursesRepository.findByIdOptional(Long.valueOf(course_id)).isEmpty()) {
            throw new ServiceException("Course not available");
        }

        if (contestantsRepository.findByEmail(contestants.getEmail()).isEmpty()) {
            contestantsRepository.persist(contestants);
        }

        contestants = contestantsRepository.findByEmail(contestants.getEmail()).get();

        final Registration registration = new Registration();
        registration.setRegistered_at(formatDateTime);
        registration.setContestants(contestants);
        registration.setCourse(coursesRepository.findById(Long.valueOf(course_id)));

       persist(registration);

       return isPersistent(registration);

    }
}
