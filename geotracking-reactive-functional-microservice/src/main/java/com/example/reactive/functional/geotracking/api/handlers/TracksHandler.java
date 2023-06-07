package com.example.reactive.functional.geotracking.api.handlers;

import com.example.reactive.functional.geotracking.common.util.DateUtil;
import com.example.reactive.functional.geotracking.dto.GeoPointResponseDTO;
import com.example.reactive.functional.geotracking.dto.TrackDTO;
import com.example.reactive.functional.geotracking.service.GeoPointService;
import com.example.reactive.functional.geotracking.service.LazyService;
import com.example.reactive.functional.geotracking.service.dto.SearchTrackByCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import java.time.Duration;
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
        final Optional<String> deviceId = request.queryParam("deviceId");
        final Optional<String> testLazyTimeParam = request.queryParam("testLazyTime");
        final Integer testLazyTime = testLazyTimeParam.isPresent() ? Integer.valueOf(testLazyTimeParam.get()) : 0;


        Mono<Integer> lazy = testLazyTime > 0 ? lazyService.sleep(testLazyTime).thenReturn(testLazyTime) : Mono.just(testLazyTime);

        if (deviceId.isPresent() && StringUtils.hasLength(deviceId.get())) {
            return lazy.flatMap( t -> geoPointService.getLastPositionByDeviceId(deviceId.get()))
                    .flatMap(geoPoint -> ServerResponse.ok().body(fromValue(geoPoint)))
                    .switchIfEmpty(ServerResponse.noContent().build());

        } else if (user.isPresent() && StringUtils.hasLength(user.get())) {
            return lazy.flatMap( t -> geoPointService.getLastPositionByUser(user.get()))
                    .flatMap(geoPoint -> ServerResponse.ok().body(fromValue(geoPoint)))
                    .switchIfEmpty(ServerResponse.noContent().build());

        } else throw new ServerWebInputException("Some parameter does not have to be empty");

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
                // delay 2 secons !!!!!!!!!!
                .body(geoPointService.findGeoPointByParameters(criteria).delayElements(Duration.ofSeconds(2)), GeoPointResponseDTO.class)
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
