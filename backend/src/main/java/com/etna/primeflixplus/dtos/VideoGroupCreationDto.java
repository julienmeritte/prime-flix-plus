package com.etna.primeflixplus.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class VideoGroupCreationDto {

    @NotNull
    String name;

    Boolean isSerie = false;
}
