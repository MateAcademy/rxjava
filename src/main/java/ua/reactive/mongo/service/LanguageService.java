package ua.reactive.mongo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.reactive.mongo.domain.Language;
import ua.reactive.mongo.repository.LanguageRepository;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;

    public Flux<Language> findAll() {
        return languageRepository.findAll();
    }

    public Mono<Language> save(Mono<Language> language) {
        //return languageRepository.saveAll(language).next();
        return language.flatMap(languageRepository::save);
    }

    public Mono<Language> findByName(String name) {
        return languageRepository.findByName(name);
    }

    public Mono<Language> deleteByName(String name) {
        return languageRepository.findByName(name)
                .flatMap(existing -> languageRepository.delete(existing).thenReturn(existing));
    }

    public Mono<Language> update(String name, Mono<Language> language) {
        return languageRepository.findByName(name)
                .flatMap(existing -> language.map(updated -> {
                    updated.setId(existing.getId());
                    return updated;
                }))
                .flatMap(languageRepository::save);
    }

}
