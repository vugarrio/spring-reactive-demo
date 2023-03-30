package com.example.reactive.functional.geotracking.service.impl;

import com.example.reactive.functional.geotracking.domain.entity.GeoPoint;
import com.example.reactive.functional.geotracking.domain.repository.GeoPointRepository;
import com.example.reactive.functional.geotracking.dto.TrackDTO;
import com.example.reactive.functional.geotracking.dto.TrackRefDTO;
import com.example.reactive.functional.geotracking.exception.GeoPointServiceException;
import com.example.reactive.functional.geotracking.service.GeoPointService;
import com.example.reactive.functional.geotracking.service.converter.GeoPointConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class GeoPointServiceImpl implements GeoPointService {

    private final GeoPointRepository geoPointRepository;

    private final GeoPointConverter geoPointconverter;

    @Override
    public Mono<TrackRefDTO> processTrack(TrackDTO track) {
        LOGGER.debug("Processing geo-tracking of user:{}, points:{}", track.getUser(), track.getPoints().size());
        String trackId = UUID.randomUUID().toString();

        TrackRefDTO trackRefDTO = new TrackRefDTO();
        trackRefDTO.setTrackId(trackId);

        //Persists tracking
        Flux<GeoPoint> savedGeos = geoPointRepository.saveAll(
                track.getPoints()
                        .stream()
                        .map(geoPoint->{
                            GeoPoint gp = geoPointconverter.toEntity(geoPoint);
                            gp.setUser(track.getUser());
                            gp.setTrackId(trackId);
                            gp.setDeviceId(track.getDeviceId());
                            gp.setCreateAt(Instant.now());
                            return gp;
                        })
                        .collect(Collectors.toList())
                )
                .onErrorResume(e->Mono.error(GeoPointServiceException::new));


        return savedGeos.then(
                Mono.just(trackRefDTO)
        );
    }
}
