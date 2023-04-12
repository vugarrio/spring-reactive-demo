package com.example.reactive.annotation.geotracking.api.controller;

import com.example.reactive.annotation.geotracking.dto.TrackResponseDTO;
import com.example.reactive.annotation.geotracking.dto.UserGeoPointResponseDTO;
import com.example.reactive.annotation.geotracking.service.GeoPointService;
import com.example.reactive.annotation.geotracking.api.TracksApi;
import com.example.reactive.annotation.geotracking.dto.TrackDTO;
import com.example.reactive.annotation.geotracking.dto.TrackRefDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;


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

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<ResponseEntity<UserGeoPointResponseDTO>> getLastPosition(String user, String deviceId, ServerWebExchange exchange) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<ResponseEntity<TrackResponseDTO>> getTrackByParams(String user, OffsetDateTime dateFrom, OffsetDateTime dateTo, ServerWebExchange exchange) {
        return null;
    }
}
