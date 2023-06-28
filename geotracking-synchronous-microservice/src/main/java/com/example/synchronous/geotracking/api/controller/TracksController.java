package com.example.synchronous.geotracking.api.controller;

import com.example.synchronous.geotracking.api.TracksApi;
import com.example.synchronous.geotracking.dto.GeoPointResponseDTO;
import com.example.synchronous.geotracking.dto.TrackDTO;
import com.example.synchronous.geotracking.dto.TrackRefDTO;
import com.example.synchronous.geotracking.service.GeoPointService;
import com.example.synchronous.geotracking.service.LazyService;
import com.example.synchronous.geotracking.service.dto.SearchTrackByCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final LazyService lazyService;

    @Override
    public ResponseEntity<TrackRefDTO> processTrack(TrackDTO trackDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(geoPointService.processTrack(trackDTO));
    }

    @Override
    public ResponseEntity<GeoPointResponseDTO> getLastPosition(String user, Integer testLazyTime) {

        // Call a lazy service to sleep the indicated
        if (Objects.nonNull(testLazyTime)) {
            lazyService.sleep(testLazyTime);
        }

        // Get last position
        GeoPointResponseDTO lastPositionByDeviceId = geoPointService.getLastPositionByUser(user);

        if (Objects.isNull(lastPositionByDeviceId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
