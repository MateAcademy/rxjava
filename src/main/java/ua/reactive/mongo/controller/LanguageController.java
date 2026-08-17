package ua.reactive.mongo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.reactive.mongo.domain.Language;
import ua.reactive.mongo.service.LanguageService;

@Slf4j
@RestController
@RequestMapping(value = "languages", produces = "application/json")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @GetMapping
    public Flux<Language> getLanguages() {
        log.warn(Thread.currentThread().getName());
        return languageService.findAll();
    }

    @PostMapping
    public Mono<ResponseEntity<Language>> create(@RequestBody Mono<Language> language) {
        return languageService.save(language)
                .map(saved -> ResponseEntity.status(HttpStatus.CREATED).body(saved));
    }

    @GetMapping("{name}")
    public Mono<ResponseEntity<Language>> show(@PathVariable String name) {
        return languageService.findByName(name)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("{name}")
    public Mono<ResponseEntity<Language>> update(@PathVariable String name, @RequestBody Mono<Language> language) {
        return languageService.update(name, language)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> delete(@PathVariable String name) {
        return languageService.deleteByName(name)
                .<ResponseEntity<Void>>map(_ -> ResponseEntity.noContent().build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
