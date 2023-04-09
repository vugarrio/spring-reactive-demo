package com.example.synchronous.geotracking.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;

/**
 * GeoPoint domain entity.
 *
 * @since 1.0.0
 */
@Document(collection = "tracks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeoPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String user;

    private Instant timestamp;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint point;

    private String trackId;

    private String deviceId;

    private Double accuracy;

    private Instant createAt;

}
