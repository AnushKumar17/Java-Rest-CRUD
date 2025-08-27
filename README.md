package com.example.crudwebflux;

import com.example.crudwebflux.model.User;
import com.example.crudwebflux.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository repo;

    @BeforeEach
    void cleanDb() {
        repo.deleteAll().block(); // clear before each test
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setName("Alice");
        user.setEmail("alice@example.com");

        webTestClient.post()
                .uri("/users")
                .body(Mono.just(user), User.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNumber()
                .jsonPath("$.name").isEqualTo("Alice")
                .jsonPath("$.email").isEqualTo("alice@example.com");
    }

    @Test
    void testGetAllUsers() {
        User u1 = new User(); u1.setName("A"); u1.setEmail("a@a.com");
        User u2 = new User(); u2.setName("B"); u2.setEmail("b@b.com");
        repo.saveAll(Mono.just(u1).concatWith(Mono.just(u2))).collectList().block();

        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("A")
                .jsonPath("$[1].name").isEqualTo("B");
    }

    @Test
    void testGetUserById() {
        User u = new User(); u.setName("John"); u.setEmail("john@example.com");
        User saved = repo.save(u).block();

        webTestClient.get()
                .uri("/users/{id}", saved.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("John")
                .jsonPath("$.email").isEqualTo("john@example.com");
    }

    @Test
    void testUpdateUser() {
        User u = new User(); u.setName("Old"); u.setEmail("old@x.com");
        User saved = repo.save(u).block();

        User update = new User(); update.setName("New"); update.setEmail("new@x.com");

        webTestClient.put()
                .uri("/users/{id}", saved.getId())
                .body(Mono.just(update), User.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("New")
                .jsonPath("$.email").isEqualTo("new@x.com");
    }

    @Test
    void testDeleteUser() {
        User u = new User(); u.setName("DeleteMe"); u.setEmail("del@x.com");
        User saved = repo.save(u).block();

        webTestClient.delete()
                .uri("/users/{id}", saved.getId())
                .exchange()
                .expectStatus().isOk();

        webTestClient.get()
                .uri("/users/{id}", saved.getId())
                .exchange()
                .expectBody().isEmpty();
    }
}
