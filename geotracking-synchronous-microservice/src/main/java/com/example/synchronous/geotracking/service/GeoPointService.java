package com.example.synchronous.geotracking.service;

import com.example.synchronous.geotracking.dto.TrackDTO;
import com.example.synchronous.geotracking.dto.TrackRefDTO;
import org.springframework.validation.annotation.Validated;

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
    TrackRefDTO processTrack(@Valid TrackDTO track);

}
