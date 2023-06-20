package com.example.reactive.functional.geotracking.api.handlers;

import com.example.reactive.functional.geotracking.common.util.DateUtil;
import com.example.reactive.functional.geotracking.dto.GeoPointResponseDTO;
import com.example.reactive.functional.geotracking.dto.TrackDTO;
import com.example.reactive.functional.geotracking.service.GeoPointService;
import com.example.reactive.functional.geotracking.service.LazyService;
import com.example.reactive.functional.geotracking.service.dto.SearchTrackByCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@Slf4j
@RequiredArgsConstructor
public class TracksHandler {

    private final GeoPointService geoPointService;

    private final ValidatorHandler validatorHandler;

    private final LazyService lazyService;


    public Mono<ServerResponse> processTrack(ServerRequest request) {
        return request.bodyToMono(TrackDTO.class)
                .doOnNext(track -> validatorHandler.validate(track))
                .flatMap(
                        geoPointService::processTrack
                )
                .flatMap(cust -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromValue(cust)));
    }


    public Mono<ServerResponse> getLastPosition(ServerRequest request) {
        final Optional<String> user = request.queryParam("user");
        final String testLazyTimeHeader = request.headers().firstHeader("testLazyTime");
        final Integer testLazyTime = NumberUtils.isParsable(testLazyTimeHeader)  ? Integer.valueOf(testLazyTimeHeader) : 0;

        // Call a lazy service to sleep the indicated
        Mono<Integer> lazy = lazyService.sleep(testLazyTime).thenReturn(testLazyTime);

        // Get last position
       if (user.isPresent() && StringUtils.hasLength(user.get())) {
            return lazy.flatMap( t -> geoPointService.getLastPositionByUser(user.get()))
                    .flatMap(geoPoint -> ServerResponse.ok().body(fromValue(geoPoint)))
                    .switchIfEmpty(ServerResponse.noContent().build());

        } else throw new ServerWebInputException("user: must not be empty");

    }


    public Mono<ServerResponse> getTrackByParams(ServerRequest request) {
        final SearchTrackByCriteria criteria = buildSearchTrackByCriteria(request);
        LOGGER.debug(":: getTrackByParams :: Params {} ", criteria);

        return geoPointService.findGeoPointByParameters(criteria).collectList()
                .flatMap(ele -> {
                    LOGGER.debug(":: getTrackByParams :: Result {} geopoints", ele.size());
                    if (!ele.isEmpty()) {
                        return ServerResponse.ok().contentType(APPLICATION_JSON).body(fromValue(ele));
                    } else return ServerResponse.noContent().build();
                });
    }


    public Mono<ServerResponse> getTrackStreamsByParams(ServerRequest request) {
        final SearchTrackByCriteria criteria = buildSearchTrackByCriteria(request);
        LOGGER.debug(":: getTrackStreamsByParams :: Params {} ", criteria);

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(geoPointService.findGeoPointByParameters(criteria), GeoPointResponseDTO.class)
                .switchIfEmpty(ServerResponse.noContent().build());
    }



    private SearchTrackByCriteria buildSearchTrackByCriteria(ServerRequest request) {
        return  SearchTrackByCriteria.builder()
                .user(request.queryParam("user").orElse(null))
                .dateFrom(request.queryParam("dateFrom")
                        .map(DateUtil::parseToOffsetDateTime)
                        .orElse(null))
                .dateTo(request.queryParam("dateTo")
                        .map(DateUtil::parseToOffsetDateTime)
                        .orElse(null))
                .build();
    }


}
