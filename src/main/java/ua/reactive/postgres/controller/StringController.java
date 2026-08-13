package ua.reactive.postgres.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class StringController {

    @GetMapping
    public Mono<List<String>> getString() {
        return Flux.range(1, 20).map(String::valueOf).collectList();
    }
}
