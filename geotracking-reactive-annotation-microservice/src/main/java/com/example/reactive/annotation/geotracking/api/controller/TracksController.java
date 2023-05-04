package com.example.reactive.annotation.geotracking.api.controller;

import com.example.reactive.annotation.geotracking.api.TracksApi;
import com.example.reactive.annotation.geotracking.service.dto.SearchTrackByCriteria;
import com.example.reactive.annotation.geotracking.dto.GeoPointResponseDTO;
import com.example.reactive.annotation.geotracking.dto.TrackDTO;
import com.example.reactive.annotation.geotracking.dto.TrackRefDTO;
import com.example.reactive.annotation.geotracking.service.GeoPointService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Flux;
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
    public Mono<ResponseEntity<GeoPointResponseDTO>> getLastPosition(String user, String deviceId, ServerWebExchange exchange) {
        if (!StringUtils.isEmpty(deviceId)) {
            return geoPointService.getLastPositionByDeviceId(deviceId)
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        } else if (!StringUtils.isEmpty(user)) {
            return geoPointService.getLastPositionByUser(user)
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        } throw new ServerWebInputException("Some parameter does not have to be empty");
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

//        return geoPointService.findGeoPointByParameters(criteria)
//                .collectList()
//                .flatMap(list -> list.isEmpty()
//                        ? Mono.just(ResponseEntity.noContent().build())
//                        : Mono.just(ResponseEntity.ok().body(Flux.fromIterable(list))));

    }
}
