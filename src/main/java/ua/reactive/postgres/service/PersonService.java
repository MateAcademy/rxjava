package ua.reactive.postgres.service;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.reactive.postgres.model.Person;
import ua.reactive.postgres.repository.PersonRepository;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository repository;

    public Mono<Void> create(Publisher<Person> personStream) {
        return repository.saveAll(personStream).then();
    }

    public Flux<Person> findAll() {
        return repository.findAll();
    }

    public Mono<Person> findById(Long id) {
        return repository.findById(id);
    }
}