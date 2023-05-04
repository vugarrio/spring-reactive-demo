package com.example.reactive.annotation.geotracking.service.converter;

import com.example.reactive.annotation.geotracking.domain.entity.GeoPoint;
import com.example.reactive.annotation.geotracking.dto.GeoPointResponseDTO;
import com.example.reactive.annotation.geotracking.dto.GeolocationDTO;
import com.example.reactive.annotation.geotracking.dto.PointDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;


/**
 * GeoPoint converter.
 *
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface GeoPointConverter {



    @Mapping(target = "longitude", source="entry.x")
    @Mapping(target = "latitude", source="entry.y")
    GeolocationDTO toDto(GeoJsonPoint entry);

    //GeoPointDTO toDto(GeoPoint entity);

    //UserGeoPointResponseDTO toUserGeoPointResponseDTO(GeoPoint entry);

    GeoPointResponseDTO toGeoPointResponseDTO(GeoPoint entry);



    default GeoPoint toEntity(PointDTO dto) {
        if ( dto == null ) {
            return null;
        }

        GeoPoint geoPoint = new GeoPoint();

        geoPoint.setAccuracy(geoPoint.getAccuracy());
        geoPoint.setTimestamp(map(dto.getTimestamp()));
        GeoJsonPoint geoJsonPoint = new GeoJsonPoint(dto.getPoint().getLongitude(), dto.getPoint().getLatitude());
        geoPoint.setPoint(geoJsonPoint);
        return geoPoint;
    }


    /*
     * Convers dates
     */
    default OffsetDateTime map(Instant instant) {
        return instant == null ? null : instant.atOffset(ZoneOffset.UTC);
    }

    default Instant map(OffsetDateTime  source) {
        return source == null ? null : source.toInstant();
    }

    default GeoJsonPoint map (PointDTO dto) {
        return new org.springframework.data.mongodb.core.geo.GeoJsonPoint(dto.getPoint().getLongitude(), dto.getPoint().getLatitude());
    }



}
