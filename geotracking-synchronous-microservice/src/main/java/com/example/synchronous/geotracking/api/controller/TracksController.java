package com.example.synchronous.geotracking.api.controller;

import com.example.synchronous.geotracking.api.TracksApi;
import com.example.synchronous.geotracking.dto.GeoPointResponseDTO;
import com.example.synchronous.geotracking.dto.TrackDTO;
import com.example.synchronous.geotracking.dto.TrackRefDTO;
import com.example.synchronous.geotracking.service.GeoPointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;

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
        return null;
    }

    @Override
    public ResponseEntity<List<GeoPointResponseDTO>> getTrackByParams(String user, OffsetDateTime dateFrom, OffsetDateTime dateTo) {
        return null;
    }

}
