package com.example.synchronous.geotracking.service;

import com.example.synchronous.geotracking.dto.GeoPointResponseDTO;
import com.example.synchronous.geotracking.dto.TrackDTO;
import com.example.synchronous.geotracking.dto.TrackRefDTO;
import com.example.synchronous.geotracking.service.dto.SearchTrackByCriteria;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

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

    /**
     * Gets the user geo-tracking points registered between to dates
     * @param criteria
     *
     * @return flux GeoPointResponseDTO
     */
    List<GeoPointResponseDTO> findGeoPointByParameters(@Valid SearchTrackByCriteria criteria);

    /**
     * Get the last geo point of a device
     * @param deviceId
     *
     * @return geo point item
     */
    GeoPointResponseDTO getLastPositionByDeviceId(@NotEmpty String deviceId);


    /**
     * Get the last geo point of a user
     * @param user
     *
     * @return geo point item
     */
    GeoPointResponseDTO getLastPositionByUser(@NotEmpty String user);

}
