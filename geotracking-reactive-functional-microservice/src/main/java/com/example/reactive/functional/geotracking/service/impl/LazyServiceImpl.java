package com.example.reactive.functional.geotracking.service.impl;

import com.example.reactive.functional.geotracking.service.LazyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
public class LazyServiceImpl implements LazyService {

    @Override
    public Mono<String> sleep(long time) {

        return Mono.delay(Duration.ofSeconds(time)).map(l -> "Sleep " + time + " seconds");
        //return Mono.fromSupplier(() -> blockingFunction(time));

    }

    private String blockingFunction(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Sleep " + time + " seconds";
    }
}
