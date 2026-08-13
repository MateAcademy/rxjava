package ua.reactive.postgres.controller;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.reactive.postgres.model.Person;
import ua.reactive.postgres.service.PersonService;

@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonService service;

    @PostMapping("person")
    Mono<Void> create(@RequestBody Publisher<Person> personStream) {
        return service.create(personStream);
    }

    @GetMapping("person")
    Flux<Person> list() {
        return service.findAll();
    }

    @GetMapping("person/{id}")
    Mono<Person> findById(@PathVariable Long id) {
        return service.findById(id);
    }
}