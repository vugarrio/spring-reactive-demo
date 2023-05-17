package com.example.synchronous.geotracking.api.controller;

import com.example.synchronous.geotracking.api.TracksApi;
import com.example.synchronous.geotracking.api.exception.ParameterException;
import com.example.synchronous.geotracking.dto.GeoPointResponseDTO;
import com.example.synchronous.geotracking.dto.TrackDTO;
import com.example.synchronous.geotracking.dto.TrackRefDTO;
import com.example.synchronous.geotracking.service.GeoPointService;
import com.example.synchronous.geotracking.service.dto.SearchTrackByCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class TracksController implements TracksApi {

    private final GeoPointService geoPointService;

    @Override
    public ResponseEntity<TrackRefDTO> processTrack(TrackDTO trackDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(geoPointService.processTrack(trackDTO));
    }

    @Override
    public ResponseEntity<GeoPointResponseDTO> getLastPosition(String user, String deviceId) {
        GeoPointResponseDTO lastPositionByDeviceId = null;
        if (!StringUtils.isEmpty(deviceId)) {
            lastPositionByDeviceId = geoPointService.getLastPositionByDeviceId(deviceId);

        } else if (!StringUtils.isEmpty(user)) {
            lastPositionByDeviceId = geoPointService.getLastPositionByUser(user);

        } else throw new ParameterException("Some parameter does not have to be empty");

        if (Objects.isNull(lastPositionByDeviceId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(lastPositionByDeviceId);
        }

    }

    @Override
    public ResponseEntity<List<GeoPointResponseDTO>> getTrackByParams(String user, OffsetDateTime dateFrom, OffsetDateTime dateTo) {
        final SearchTrackByCriteria criteria = SearchTrackByCriteria.builder()
                .user(user)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build();

        final List<GeoPointResponseDTO> result = geoPointService.findGeoPointByParameters(criteria);

        if (CollectionUtils.isEmpty(result)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

}
