package com.example.synchronous.geotracking.service.impl;

import com.example.synchronous.geotracking.common.util.DateUtil;
import com.example.synchronous.geotracking.domain.entity.GeoPoint;
import com.example.synchronous.geotracking.domain.repository.GeoPointRepository;
import com.example.synchronous.geotracking.dto.GeoPointResponseDTO;
import com.example.synchronous.geotracking.dto.TrackDTO;
import com.example.synchronous.geotracking.dto.TrackRefDTO;
import com.example.synchronous.geotracking.service.GeoPointService;
import com.example.synchronous.geotracking.service.converter.GeoPointConverter;
import com.example.synchronous.geotracking.service.dto.SearchTrackByCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoPointServiceImpl implements GeoPointService {

    private final GeoPointRepository geoPointRepository;

    private final GeoPointConverter geoPointconverter;

    @Override
    public TrackRefDTO processTrack(TrackDTO track) {
        LOGGER.debug("Processing geo-tracking of user:{}, points:{}", track.getUser(), track.getPoints().size());
        String trackId = UUID.randomUUID().toString();

        TrackRefDTO trackRefDTO = new TrackRefDTO();
        trackRefDTO.setTrackId(trackId);

        geoPointRepository.saveAll(track.getPoints()
                .stream()
                .map(geoPoint -> {
                    GeoPoint gp = geoPointconverter.toEntity(geoPoint);
                    gp.setUser(track.getUser());
                    gp.setTrackId(trackId);
                    gp.setDeviceId(track.getDeviceId());
                    gp.setCreateAt(Instant.now());
                    return gp;
                })
                .collect(Collectors.toList())
        );

        return trackRefDTO;
    }


    @Override
    public List<GeoPointResponseDTO> findGeoPointByParameters(SearchTrackByCriteria criteria) {
        LOGGER.debug(":: findGeoPointByParameters :: Param [criteria:{}]", criteria);
        return geoPointRepository.findByTimestampBetweenAndUserOrderByTimestamp(
                        DateUtil.parseToInstant(criteria.getDateFrom()),
                        DateUtil.parseToInstant(criteria.getDateTo()),
                        criteria.getUser())
                .stream().map(geoPointconverter::toGeoPointResponseDTO).collect(Collectors.toList());

    }

    @Override
    public GeoPointResponseDTO getLastPositionByDeviceId(String deviceId) {
        LOGGER.debug(":: getLastPositionByDeviceId :: Param [deviceId:{}]", deviceId);
        return geoPointconverter.toGeoPointResponseDTO(geoPointRepository.getFirstByDeviceIdOrderByTimestampDesc(deviceId));
    }

    @Override
    public GeoPointResponseDTO getLastPositionByUser(String user) {
        LOGGER.debug(":: getLastPositionByUser :: Param [user:{}]", user);
        return geoPointconverter.toGeoPointResponseDTO(geoPointRepository.getFirstByUserOrderByTimestampDesc(user));
    }
}
