package com.example.synchronous.geotracking.domain.repository;


import com.example.synchronous.geotracking.domain.entity.GeoPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.time.Instant;
import java.util.List;

@Repository
public interface GeoPointRepository extends MongoRepository<GeoPoint, String> {

    List<GeoPoint> findByTimestampBetweenAndUserOrderByTimestamp(Instant from, Instant to, String user);

    GeoPoint getFirstByDeviceIdOrderByTimestampDesc(String deviceId);

    GeoPoint getFirstByUserOrderByTimestampDesc(String user);

}
