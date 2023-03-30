package com.example.reactive.functional.geotracking.api.routers;

import com.example.reactive.functional.geotracking.api.handlers.TracksHandler;
import com.example.reactive.functional.geotracking.dto.ErrorDTO;
import com.example.reactive.functional.geotracking.dto.TrackDTO;
import com.example.reactive.functional.geotracking.dto.TrackRefDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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


}
