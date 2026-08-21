package ua.reactive.postgres.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ua.reactive.postgres.model.Person;

public interface PersonRepository extends ReactiveCrudRepository<Person, Long> {
}