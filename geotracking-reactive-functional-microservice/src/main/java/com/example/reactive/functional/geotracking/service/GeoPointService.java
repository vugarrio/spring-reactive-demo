package com.example.reactive.functional.geotracking.service;

import com.example.reactive.functional.geotracking.dto.TrackDTO;
import com.example.reactive.functional.geotracking.dto.TrackRefDTO;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * Geo-tracking service interface.
 */
@Validated
public interface GeoPointService {

    /**
     * Process a collection of geo-tracking points
     *
     * @param track A collection of geo-tracking points of a user
     *
     * @return
     */
    Mono<TrackRefDTO> processTrack(@Valid TrackDTO track);

}
