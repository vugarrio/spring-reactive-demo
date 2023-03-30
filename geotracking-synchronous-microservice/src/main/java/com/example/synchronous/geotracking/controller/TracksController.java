package com.example.synchronous.geotracking.controller;

import com.example.synchronous.geotracking.api.TracksApi;
import com.example.synchronous.geotracking.dto.TrackDTO;
import com.example.synchronous.geotracking.dto.TrackRefDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
public class TracksController implements TracksApi {

    @Override
    public ResponseEntity<TrackRefDTO> processTrack(TrackDTO trackDTO) {
        return null;
    }

}
