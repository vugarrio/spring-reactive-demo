package com.example.reactive.annotation.geotracking.service;

import com.example.reactive.annotation.geotracking.dto.GeoPointResponseDTO;
import com.example.reactive.annotation.geotracking.dto.TrackDTO;
import com.example.reactive.annotation.geotracking.dto.TrackRefDTO;
import com.example.reactive.annotation.geotracking.service.dto.SearchTrackByCriteria;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

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


    /**
     * Gets the user geo-tracking points registered between to dates
     * @param criteria
     *
     * @return flux GeoPointResponseDTO
     */
    Flux<GeoPointResponseDTO> findGeoPointByParameters(@Valid SearchTrackByCriteria criteria);

    /**
     * Get the last geo point of a device
     * @param deviceId
     *
     * @return geo point item
     */
    Mono<GeoPointResponseDTO> getLastPositionByDeviceId(@NotEmpty String deviceId);


    /**
     * Get the last geo point of a user
     * @param user
     *
     * @return geo point item
     */
    Mono<GeoPointResponseDTO> getLastPositionByUser(@NotEmpty String user);
}
