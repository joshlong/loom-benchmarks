package com.example.loom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.NativeDetector;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class LoomApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoomApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(@Value("${spring.threads.virtual.enabled:false}") boolean enabled) {
        return args -> System.out.println("native? " +
                                          NativeDetector.inNativeImage() + " virtual threads? " + enabled);
    }

    @Bean
    RestClient restClient(@Value("${service.url}") URI uri, RestClient.Builder builder) {
        return builder
                .baseUrl(uri.toString())
                .build();
    }

    private final Map<String, Set<String>> threads = new ConcurrentHashMap<>();


    private void note() {
        var currentThread = Thread.currentThread();
        var key = Long.toString(currentThread.threadId());
        this.threads.computeIfAbsent(key, s -> new HashSet<>()).add(currentThread.toString());
    }

    @Bean
    RouterFunction<ServerResponse> httpEndpoints(RestClient rc) {
        return RouterFunctions
                .route()
                .GET("/threads", r -> ServerResponse.ok().body(this.threads))
                .GET("/block", r -> {
                    var delay = Integer.parseInt(r.param("delay").orElse("1"));
                    note();
                    var launchRequest = rc
                            .get()
                            .uri("/delay/" + delay)
                            .retrieve()
                            .toEntity(String.class);
                    note();
                    Assert.state(launchRequest.getStatusCode().is2xxSuccessful(), "the request must be successful");
                    return ServerResponse
                            .ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(Objects.requireNonNull(launchRequest.getBody()));
                })
                .build();
    }
}