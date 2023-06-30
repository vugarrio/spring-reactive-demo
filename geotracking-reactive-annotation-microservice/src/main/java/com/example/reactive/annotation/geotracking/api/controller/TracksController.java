package com.example.reactive.annotation.geotracking.api.controller;

import com.example.reactive.annotation.geotracking.api.TracksApi;
import com.example.reactive.annotation.geotracking.dto.GeoPointResponseDTO;
import com.example.reactive.annotation.geotracking.dto.TrackDTO;
import com.example.reactive.annotation.geotracking.dto.TrackRefDTO;
import com.example.reactive.annotation.geotracking.service.GeoPointService;
import com.example.reactive.annotation.geotracking.service.LazyService;
import com.example.reactive.annotation.geotracking.service.dto.SearchTrackByCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
public class  TracksController implements TracksApi {

    private final GeoPointService geoPointService;

    private final LazyService lazyService;

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
    public Mono<ResponseEntity<GeoPointResponseDTO>> getLastPosition(String user, Integer testLazyTime, ServerWebExchange exchange) {

        // Call a lazy service to sleep the indicated
        return lazyService.sleep(testLazyTime).thenReturn(testLazyTime)
                .flatMap( t ->geoPointService.getLastPositionByUser(user))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<ResponseEntity<Flux<GeoPointResponseDTO>>> getTrackByParams(String user, OffsetDateTime dateFrom, OffsetDateTime dateTo, ServerWebExchange exchange) {
         final SearchTrackByCriteria criteria = SearchTrackByCriteria.builder()
                .user(user)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build();

        return Mono.just(ResponseEntity.ok(geoPointService.findGeoPointByParameters(criteria)));
    }
}
