package com.example.reactive.functional.geotracking.api.routers;

import com.example.reactive.functional.geotracking.api.handlers.TracksHandler;
import com.example.reactive.functional.geotracking.dto.ErrorDTO;
import com.example.reactive.functional.geotracking.dto.GeoPointResponseDTO;
import com.example.reactive.functional.geotracking.dto.TrackDTO;
import com.example.reactive.functional.geotracking.dto.TrackRefDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class TracksRlouter {


    @Bean
    @RouterOperation(
            operation = @Operation(
                    operationId = "processTrack",
                    summary = "Process a track",
                    tags = { "Tracking" },

                    requestBody = @RequestBody(description = "Track", content = @Content(schema = @Schema(implementation = TrackDTO.class))),

                    responses = {
                            @ApiResponse(responseCode = "201", description = "Successful operation", content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = TrackRefDTO.class))
                            }),
                            @ApiResponse(responseCode = "400", description = "Bad input parameters.", content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
                            })
                    }
            )
    )
    public RouterFunction<ServerResponse> processTrack(TracksHandler tracksHandler) {
        return RouterFunctions.route(POST("/tracks").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), tracksHandler::processTrack);
    }



    @Bean
    @RouterOperation(
            operation = @Operation(
                    operationId = "getLastPosition",
                    summary = "Get the last position of a device or user",
                    description = "Get the last position of a device, you can filter by user or by deviceId. But it is mandatory to filter by one of the two parameters.",
                    tags = { "Tracking" },

                    parameters = {
                            @Parameter(in = ParameterIn.QUERY, name = "user", description = "User Id", required = false),
                            @Parameter(in = ParameterIn.QUERY, name = "deviceId", description = "Device Id", required = false),
                            @Parameter(in = ParameterIn.QUERY, name = "testLazyTime", description = "Time in seconds that the service is lazy", required = false)
                    },

                    responses = {
                            @ApiResponse(responseCode = "200", description = "Return one GeoPoint user item", content = @Content(schema = @Schema(implementation = GeoPointResponseDTO.class))),
                            @ApiResponse(responseCode = "204", description = "Empty results."),
                            @ApiResponse(responseCode = "400", description = "Bad input parameters.")
                    }
            )
    )
    public RouterFunction<ServerResponse> getLastPosition(TracksHandler tracksHandler) {
        return RouterFunctions.route(GET("/tracks/lastPosition").and(accept(APPLICATION_JSON)), tracksHandler::getLastPosition);
    }


    @Bean
    @RouterOperation(operation = @Operation(
                    operationId = "getTrackByParams",
                    summary = "Get the track of a user in a time interval",
                    tags = { "Tracking" },
                    parameters = {
                            @Parameter(in = ParameterIn.QUERY, name = "user", description = "User Id", required = true),
                            @Parameter(in = ParameterIn.QUERY, name = "dateFrom", description = "Track start date", required = true, example = "2021-07-05T07:30:00Z"),
                            @Parameter(in = ParameterIn.QUERY, name = "dateTo", description = "Track end date", required = true, example = "2021-07-05T18:30:00Z")

                    },
                    responses = {
                            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = GeoPointResponseDTO.class))),
                            @ApiResponse(responseCode = "204", description = "Empty results.")
                    }
            )
    )
    public RouterFunction<ServerResponse> getTrackByParams(TracksHandler tracksHandler) {
        return RouterFunctions.route(GET("/tracks").and(accept(APPLICATION_JSON)), tracksHandler::getTrackByParams);
    }


    @Bean
    @RouterOperation(operation = @Operation(
                    operationId = "getTrackStreamByParams",
                    summary = "Get the track stream of a user in a time interval",
                    tags = { "Tracking" },
                    parameters = {
                            @Parameter(in = ParameterIn.QUERY, name = "user", description = "User Id", required = true),
                            @Parameter(in = ParameterIn.QUERY, name = "dateFrom", description = "Track start date", required = true, example = "2021-07-05T07:30:00Z"),
                            @Parameter(in = ParameterIn.QUERY, name = "dateTo", description = "Track end date", required = true, example = "2021-07-05T18:30:00Z")

                    },
                    responses = {
                            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = GeoPointResponseDTO.class)))
                    }
            )
    )
    public RouterFunction<ServerResponse> getTrackStreamByParams(TracksHandler tracksHandler) {
        return RouterFunctions.route(GET("/tracks/stream").and(accept(MediaType.TEXT_EVENT_STREAM)), tracksHandler::getTrackStreamsByParams);
    }

}
