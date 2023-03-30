package com.example.reactive.functional.geotracking.domain.repository;

import com.example.reactive.functional.geotracking.domain.entity.GeoPoint;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Repository
public interface GeoPointRepository extends ReactiveSortingRepository<GeoPoint, String> {

    Flux<GeoPoint> findByTimestampBetweenAndUserOrderByTimestamp(Instant from, Instant to, String user);

    Mono<GeoPoint> getFirstByDeviceIdOrderByTimestampDesc(String deviceId);

    Mono<GeoPoint> getFirstByUserOrderByTimestampDesc(String user);

}
