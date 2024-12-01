package com.etna.primeflixplus.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QualityDto {
    @NotNull
    Integer id;

    @NotNull
    String quality;
}
