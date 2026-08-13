package ua.reactive.mongo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.reactive.mongo.domain.Language;
import ua.reactive.mongo.service.LanguageService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(LanguageController.class)
class LanguageControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
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
    void getLanguages_shouldReturn200WithList() {
        when(languageService.findAll()).thenReturn(Flux.just(java));

        webTestClient.get().uri("/languages")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Language.class)
                .hasSize(1);
    }

    @Test
    void show_shouldReturn200_whenFound() {
        when(languageService.findByName("Java")).thenReturn(Mono.just(java));

        webTestClient.get().uri("/languages/Java")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Language.class)
                .value(lang -> {
                    assert lang.getName().equals("Java");
                    assert lang.getCreator().equals("James Gosling");
                });
    }

    @Test
    void show_shouldReturn404_whenNotFound() {
        when(languageService.findByName("Unknown")).thenReturn(Mono.empty());

        webTestClient.get().uri("/languages/Unknown")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void create_shouldReturn201() {
        when(languageService.save(any())).thenReturn(Mono.just(java));

        webTestClient.post().uri("/languages")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(java)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Language.class)
                .value(lang -> {
                    assert lang.getName().equals("Java");
                    assert lang.getCreator().equals("James Gosling");
                });
    }

    @Test
    void update_shouldReturn200_whenFound() {
        when(languageService.update(eq("Java"), any())).thenReturn(Mono.just(java));

        webTestClient.put().uri("/languages/Java")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(java)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Language.class)
                .value(lang -> {
                    assert lang.getName().equals("Java");
                    assert lang.getCreator().equals("James Gosling");
                });
    }

    @Test
    void update_shouldReturn404_whenNotFound() {
        when(languageService.update(eq("Unknown"), any())).thenReturn(Mono.empty());

        webTestClient.put().uri("/languages/Unknown")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(java)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void delete_shouldReturn204_whenFound() {
        when(languageService.deleteByName("Java")).thenReturn(Mono.just(java));

        webTestClient.delete().uri("/languages/Java")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void delete_shouldReturn404_whenNotFound() {
        when(languageService.deleteByName("Unknown")).thenReturn(Mono.empty());

        webTestClient.delete().uri("/languages/Unknown")
                .exchange()
                .expectStatus().isNotFound();
    }
}
