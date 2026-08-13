package ua.reactive.mongo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ua.reactive.mongo.domain.Language;

public interface LanguageRepository extends ReactiveMongoRepository<Language, String> {

    Mono<Language> findByName(String name);

    Mono<Void> deleteByName(String name);
}
