package com.example.reactive.annotation.geotracking.api.controller;

import com.example.reactive.annotation.geotracking.service.GeoPointService;
import com.example.reactive.annotation.geotracking.api.TracksApi;
import com.example.reactive.annotation.geotracking.dto.TrackDTO;
import com.example.reactive.annotation.geotracking.dto.TrackRefDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
public class  TracksController implements TracksApi {

    private final GeoPointService geoPointService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<ResponseEntity<TrackRefDTO>> processTrack(TrackDTO trackDTO, ServerWebExchange exchange) {
        return geoPointService.processTrack(trackDTO).map(ResponseEntity::ok);
    }
}
