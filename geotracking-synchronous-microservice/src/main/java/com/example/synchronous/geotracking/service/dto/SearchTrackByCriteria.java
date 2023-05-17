package com.example.synchronous.geotracking.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchTrackByCriteria {

    @NotEmpty
    private String user;

    @NotNull
    private OffsetDateTime dateFrom;

    @NotNull
    private OffsetDateTime dateTo;

}
