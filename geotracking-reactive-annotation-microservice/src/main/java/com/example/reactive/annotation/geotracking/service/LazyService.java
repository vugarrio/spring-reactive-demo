package com.example.reactive.annotation.geotracking.service;

import reactor.core.publisher.Mono;

public interface LazyService {

    /**
     *
     * @param time in secons
     * @return description time
     */
    Mono<String> sleep(long time);

}
