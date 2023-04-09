package com.example.reactive.functional.geotracking.api.handlers;

import com.example.reactive.functional.geotracking.dto.TrackDTO;
import com.example.reactive.functional.geotracking.service.GeoPointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@Slf4j
@RequiredArgsConstructor
public class TracksHandler {

    private final GeoPointService geoPointService;

    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> processTrack(ServerRequest request) {
        return request.bodyToMono(TrackDTO.class)
                .doOnNext(track -> validatorHandler.validate(track))
                .flatMap(
                        geoPointService::processTrack
                )
                .flatMap(cust -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromValue(cust)));
    }

}
