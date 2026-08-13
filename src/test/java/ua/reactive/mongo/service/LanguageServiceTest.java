package ua.reactive.mongo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ua.reactive.mongo.domain.Language;
import ua.reactive.mongo.repository.LanguageRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LanguageServiceTest {

    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private LanguageService languageService;

    private Language java;

    @BeforeEach
    void setUp() {
        java = new Language();
        java.setId("1");
        java.setName("Java");
        java.setCreator("James Gosling");
        java.setFeature("OOP");
    }

    @Test
    void findAll_shouldReturnAllLanguages() {
        when(languageRepository.findAll()).thenReturn(Flux.just(java));

        StepVerifier.create(languageService.findAll())
                .expectNext(java)
                .verifyComplete();
    }

    @Test
    void save_shouldSaveLanguage() {
        when(languageRepository.save(java)).thenReturn(Mono.just(java));

        StepVerifier.create(languageService.save(Mono.just(java)))
                .expectNext(java)
                .verifyComplete();
    }

    @Test
    void findByName_shouldReturnLanguage() {
        when(languageRepository.findByName("Java")).thenReturn(Mono.just(java));

        StepVerifier.create(languageService.findByName("Java"))
                .expectNext(java)
                .verifyComplete();
    }

    @Test
    void findByName_shouldReturnEmpty_whenNotFound() {
        when(languageRepository.findByName("Unknown")).thenReturn(Mono.empty());

        StepVerifier.create(languageService.findByName("Unknown"))
                .verifyComplete();
    }

    @Test
    void deleteByName_shouldReturnDeletedLanguage() {
        when(languageRepository.findByName("Java")).thenReturn(Mono.just(java));
        when(languageRepository.deleteByName("Java")).thenReturn(Mono.empty());

        StepVerifier.create(languageService.deleteByName("Java"))
                .expectNext(java)
                .verifyComplete();
    }

    @Test
    void deleteByName_shouldReturnEmpty_whenNotFound() {
        when(languageRepository.findByName("Unknown")).thenReturn(Mono.empty());

        StepVerifier.create(languageService.deleteByName("Unknown"))
                .verifyComplete();
    }

    @Test
    void update_shouldUpdateLanguage() {
        Language updated = new Language();
        updated.setName("Java");
        updated.setCreator("Updated Creator");
        updated.setFeature("Updated Feature");

        when(languageRepository.findByName("Java")).thenReturn(Mono.just(java));
        when(languageRepository.save(any())).thenReturn(Mono.just(updated));

        StepVerifier.create(languageService.update("Java", Mono.just(updated)))
                .expectNext(updated)
                .verifyComplete();
    }

    @Test
    void update_shouldReturnEmpty_whenNotFound() {
        Language updated = new Language();
        when(languageRepository.findByName("Unknown")).thenReturn(Mono.empty());

        StepVerifier.create(languageService.update("Unknown", Mono.just(updated)))
                .verifyComplete();
    }
}
